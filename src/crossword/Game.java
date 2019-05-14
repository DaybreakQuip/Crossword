package crossword;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import edu.mit.eecs.parserlib.UnableToParseException;

/**
 * TODO: Make this class mutable for future milestones (?)
 * An immutable Game class representing a single Crossword Game
 *
 */
public class Game {
    public static final String ENTRY_DELIM = "~";
    public static final String WORD_DELIM = "`";
    public static final String RESPONSE_DELIM = ";";
    private final Set<String> players; // set of all playerIDs currently logged in
    private final Map<String, Puzzle> puzzles; // map of puzzleID : Puzzle
    private final Map<String, String> playerToMatch; // map of playerID : match_id
    private final Map<String, Match> matches; //  map of match_id : match
    private Set<WatchListener> listeners;
    private final Map<String, WaitListener> waitListeners;
    
    /**
     * Main method. Makes the Game from Puzzle objects from parsing.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     * @throws IOException if .puzzle file cannot be opened
     */
    public static void main(final String[] args) throws UnableToParseException, IOException {
        System.out.println(Game.parseGameFromFiles("puzzles/").getPuzzleNames());
    }
    
    /**
     * @param directory the folder to the puzzles
     * @return a new Game with all puzzles parsed from files inside puzzles/
     * @throws UnableToParseException Puzzle cannot be parsed
     * @throws IOException File cannot be read
     */
    public static Game parseGameFromFiles(String directory) throws UnableToParseException, IOException {
        // Do something
        HashMap<String, Puzzle> puzzles = new HashMap<>();
        File folder = new File(directory);
        File[] listofPuzzles = folder.listFiles();
        for (File file : listofPuzzles) {
            if (file.isFile()) {
                Puzzle newPuzzle = Puzzle.parseFromFile(directory + file.getName()); 
                if (newPuzzle.isConsistent()) {
                    puzzles.put(newPuzzle.getName(), newPuzzle);
                }
            }
        }
        return new Game(puzzles);
    }
    
    // Abstraction function:
    //    AF(puzzles, playerToMatch, matches) = a crossword game with multiple crossword puzzles, where each entry in puzzles 
    //                  represents a mapping of puzzle name to the crossword puzzle it represents, and each entry in playerToMatch represents
    //                  a mapping of a current player to the match it represents, and matches represents a mapping of a match name to an object
    //                  representation of a match 
    // Representation invariant:
    //  true
    // Safety from rep exposure:
    //  all fields are final and private
    //  puzzles map is mutable, but defensive copies are made in getPuzzles() to not return the original
    //      puzzles
    //  puzzles's keys and values are immutable types (String and Puzzle respectively)
    //Thread Safety Argument:
    //  uses monitor pattern
    
    /**
     * Creates a new Game
     * @param puzzles map of name : puzzles that are valid (consistent)
     */
    public Game(Map<String, Puzzle> puzzles) {
        this.puzzles = Collections.unmodifiableMap(new HashMap<>(puzzles));
        this.matches = new HashMap<String, Match>();
        this.playerToMatch = new HashMap<String, String>();
        this.players = new HashSet<String>();
        this.listeners = new HashSet<>();
        this.waitListeners = new HashMap<>();
    }
    
    /**
     * Returns a puzzle with a specific format where every entry is separate by new lines and no 
     * words are revealed
     * @param name the name of the puzzle
     * @return string with format: length, clue, orientation, row, col\n
     *         e.g: "4, "twinkle twinkle", ACROSS, 0, 1\n"
     */
    public synchronized String getPuzzleForResponse(String name) {
        Puzzle puzzle = puzzles.get(name);
        String puzzleString = "";
        for (Map.Entry<Integer, PuzzleEntry> entry: puzzle.getEntries().entrySet()) {
            Integer id = entry.getKey();
            PuzzleEntry puzzleEntry = entry.getValue();
            puzzleString += id + WORD_DELIM + puzzleEntry.getWord().length() + WORD_DELIM + puzzleEntry.getClue() + WORD_DELIM + 
                    puzzleEntry.getOrientation() + WORD_DELIM + puzzleEntry.getPosition().getRow() 
                            + WORD_DELIM + puzzleEntry.getPosition().getCol() + ENTRY_DELIM;
        }
        return puzzleString.substring(0,puzzleString.length()-1);
    }
    
    /**
     * Returns puzzle id from match with match id
     * @param matchID the id of the match
     * @return puzzle of the given match
     */
    public synchronized String getPuzzleFromMatchID(String matchID) {
        return matches.get(matchID).getPuzzleForResponse();
    }

    /**
     * Gets names of all puzzles and descriptions that are waiting for another player
     * @return String with format: 
     *      match ::= match_ID WORD_DELIM description;
     *      response ::= match (ENTRY_DELIM match)*;
     */
    public synchronized String getAvailableMatchesForResponse(){
        StringBuilder responseBuilder = new StringBuilder();
        for (Match match : matches.values()) {
            if (responseBuilder.length() > 0) { // Add an entry delim if the entry is not the first one
                responseBuilder.append(ENTRY_DELIM);
            }
            // Only add matches that are waiting for another player
            if (match.isWaiting()) {
                responseBuilder.append(match.getMatchId() + WORD_DELIM + match.getDescription());
            } else {
                System.out.println("match is full: " + match.getMatchId()); 
            }
        }
        return responseBuilder.toString();
    }
        
    /**
     * Allows a new player into the game
     * @param playerID player name
     * @return true if player managed to join the game, false otherwise
     */
    public synchronized boolean login(String playerID) {
        if (players.contains(playerID)) { // Player already logged in!
            return false;
        }
        // player successfully logged in
        players.add(playerID);
        return true;
    }
    /**
     * Player tries to join a match with match id
     * @param playerID ID of the player who wants to join a game
     * @param matchID ID of the puzzle that the player wants to join
     * @return true if successfully joined, false otherwise
     * @throws IOException 
     */
    public synchronized boolean joinMatch(String playerID, String matchID) throws IOException {
        boolean joined = matches.get(matchID).joinMatch(playerID);
        if (joined) {
            playerToMatch.put(playerID, matchID);
            callWatchListeners();
            Match match = matches.get(matchID);
            // Available matches just changed;
            callWatchListeners();
            //callWaitListener(match.getPlayerOne());
        }
        return joined;
    }
    
    /**
     * Player tries to create a match with matchID as the name
     * @param playerID ID of the player who wants to join a game
     * @param matchID ID of the puzzle that the player wants to join
     * @param puzzleID puzzle name
     * @param description description of the match
     * @return true if successfully joined, false otherwise
     * @throws IOException 
     */
    public synchronized boolean createMatch(String playerID, String matchID, String puzzleID, String description) throws IOException {
        if (playerToMatch.containsKey(playerID) || matches.containsKey(matchID)) {
            return false;
        }
        playerToMatch.put(playerID, matchID);
        matches.put(matchID, new Match(matchID, description, puzzles.get(puzzleID), playerID));
        // TODO add waitListener
        callWatchListeners();
        return true;
        
    }
    
    /**
     * Disconnects a player from the game
     * @param playerID player name
     * @return true if player managed to quit the game, false otherwise
     */
    public synchronized boolean logout(String playerID) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Player tries to guess a word in the puzzle
     * @param playerID player name
     * @param wordID the ID of the word to attempt to solve
     * @param word the word that is guessed
     * @return true if player managed to guess, false otherwise
     */
    public synchronized boolean tryWord(String playerID, String wordID, String word) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Player tries to challenge another player's word in the puzzle
     * @param playerID player name
     * @param wordID the ID of the word to attempt to challenge
     * @param word the word that the player uses to challenge
     * @return true if player managed to challenge, false otherwise
     */
    public synchronized boolean challengeWord(String playerID, String wordID, String word) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Gets a player's score
     * @param playerID player name
     * @return the score of the player
     */
    public synchronized String showScore(String playerID) {
        throw new RuntimeException("Not Implemented");

    }
    
    /**
     * @return set of the names of all puzzles in the game
     */
    public synchronized Set<String> getPuzzleNames() {
        return puzzles.keySet();
    }
    
    /**
     * Returns a PlayablePuzzle with a specific format where every entry is separate by new lines and no 
     * words are revealed
     * @param playerID name of player
     * @return string with format: length, clue, orientation, row, col\n
     *         e.g: "4, "twinkle twinkle", ACROSS, 0, 1\n"
     */
    public synchronized String getMatchPuzzleForResponse(String playerID) {
        if (!playerToMatch.containsKey(playerID)) {
            throw new RuntimeException("PlayerID is not currently in a match");
        }
        String matchID = playerToMatch.get(playerID);
        Match match = matches.get(matchID);
        return match.getPuzzleForResponse();
    }

    /** A watch listener for the board  */
    public interface WatchListener {
        /** 
         * Called when the available matches in the game changes.
         * A change is defined as when a new match becomes available or an available match becomes full
         * @return String of the available matches since last change
         */
        public void onChange();
    }
    /**
     * Adds a listener for changes to available matches in the game
     * TODO: Remove?
     * @param listener Adds a new listener
     */
    public synchronized void addWatchListener(WatchListener listener) {
        listeners.add(listener);
    }
    
    private synchronized void callWatchListeners() throws IOException{
        for (WatchListener listener : listeners) {
            listener.onChange();
        }
    }
    
    /** A watch listener for the board  */
    public interface WaitListener {
        /** 
         * Called when the available matches in the game changes.
         * A change is defined as when a new match becomes available or an available match becomes full
         * @return String of the available matches since last change
         */
        public void onChange();
    }
    /**
     * Adds a listener for a player to wait for another player to join their match
     * TODO: Remove?
     * @param playerID id of the player
     * @param listener Adds a new listener
     */
    public synchronized void addWaitListener(String playerID, WaitListener listener) {
        waitListeners.put(playerID, listener);
    }
    
    /**
     * Calls the wait listener corresponding to the player ID
     * @param playerID the ID of the player
     * @throws IOException calling wait listener does not work out
     */
    private synchronized void callWaitListener(String playerID) throws IOException{
        System.out.println("id: "+playerID);
        if (!waitListeners.containsKey(playerID)) {
            throw new RuntimeException("PlayerID must be waiting to call their listener");
        }
        WaitListener listener = waitListeners.get(playerID);
        listener.onChange();
    }
    
    /**
     * @return string of puzzle names with format:
     *  response = puzzleName (ENTRY_DELIM puzzleName)*
     */
    public synchronized String getPuzzleNamesForResponse() {
        StringBuilder builder = new StringBuilder();
        for (String puzzleName : puzzles.keySet()) {
            builder.append(puzzleName + Game.ENTRY_DELIM);
        }
        builder.deleteCharAt(builder.length()-1);
        return builder.toString();
    }
    
    /**
     * @return game with puzzle names and their representation separated by newlines
     */
    @Override
    public synchronized String toString() {
        StringBuilder gameString = new StringBuilder();
        for (String puzzleName : puzzles.keySet()) {
            Puzzle puzzle = puzzles.get(puzzleName);
            gameString.append(puzzleName + "\n" + puzzle.toString() + "\n\n");
        }

        // Remove the string minus the newline at the end
        return gameString.substring(0,  gameString.length()-1);
    }
}
