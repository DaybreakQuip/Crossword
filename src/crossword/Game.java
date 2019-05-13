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
    private final Map<String, Puzzle> puzzles; // map of name : puzzle
    private final Map<String, String> playerToMatch; // map of player_id : match_id
    private final Map<String, Match> matches; //  map of match_id : match
    private static final String ENTRY_DELIM = "~";
    private static final String WORD_DELIM = "`";
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
    //TODO: Monitor pattern
    
    /**
     * Creates a new Game
     * @param puzzles map of name : puzzles that are valid (consistent)
     */
    public Game(Map<String, Puzzle> puzzles) {
        this.puzzles = Collections.unmodifiableMap(new HashMap<>(puzzles));
    }
    
    /**
     * Returns a puzzle with specified name
     * @param name the name of the puzzle
     * @return puzzle from the game if the name exists
     */
    public Puzzle getPuzzle(String name) {
        return this.puzzles.get(name);
    }
    
    /**
     * Returns a puzzle with a specific format where every entry is separate by new lines and no 
     * words are revealed
     * @param name the name of the puzzle
     * @return string with format: length, clue, orientation, row, col\n
     *         e.g: "4, "twinkle twinkle", ACROSS, 0, 1\n"
     */
    public String getPuzzleForResponse(String name) {
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
     * Gets names of all puzzles and descriptions that are awaiting another player
     * @return String with format: 
     *      match ::= match_ID WORD_DELIM description;
     *      response ::= match (ENTRY_DELIM match)*;
     */
    public String getAvailableMatchesForResponse(){
        
    }
    /**
     * @param playerID player name
     * @return true if player managed to join the game, false otherwise
     */
    public boolean login(String playerID) {
        throw new RuntimeException("Not Implemented");
    }
    /**
     * Player tries to join a match by a match's name
     * @param playerID ID of the player who wants to join a game
     * @param matchID ID of the puzzle that the player wants to join
     * @return true if successfully joined, false otherwise
     */
    public boolean joinMatch(String playerID, String matchID) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Player tries to create a match with matchID as the name
     * @param playerID ID of the player who wants to join a game
     * @param matchID ID of the puzzle that the player wants to join
     * @return true if successfully joined, false otherwise
     */
    public boolean createMatch(String playerID, String matchID) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Disconnects a player from the game
     * @param playerID player name
     * @return true if player managed to quit the game, false otherwise
     */
    public boolean logout(String playerID) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Player tries to guess a word in the puzzle
     * @param playerID player name
     * @param wordID the ID of the word to attempt to solve
     * @param word the word that is guessed
     * @return true if player managed to guess, false otherwise
     */
    public boolean tryWord(String playerID, String wordID, String word) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Player tries to challenge another player's word in the puzzle
     * @param playerID player name
     * @param wordID the ID of the word to attempt to challenge
     * @param word the word that the player uses to challenge
     * @return true if player managed to challenge, false otherwise
     */
    public boolean challengeWord(String playerID, String wordID, String word) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Gets a player's score
     * @param playerID player name
     * @return the score of the player
     */
    public String showScore(String playerID) {
        throw new RuntimeException("Not Implemented");

    }
    /**
     * @return set of the names of all puzzles in the game
     */
    public Set<String> getPuzzleNames() {
        return puzzles.keySet();
    }
    /**
     * Returns a PlayablePuzzle with a specific format where every entry is separate by new lines and no 
     * words are revealed
     * @param playerID name of player
     * @return string with format: length, clue, orientation, row, col\n
     *         e.g: "4, "twinkle twinkle", ACROSS, 0, 1\n"
     */
    public String getMatchPuzzleForResponse(String playerID) {
        throw new RuntimeException("Not Implemented");
    }
    @Override
    public int hashCode() {
        return puzzles.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof Game && sameValue((Game) other);
    }

    /**
     * @param other the other game to compare to
     * @return true if this and other game are equal and false otherwise
     */
    public boolean sameValue(Game other) {
        return puzzles.equals(other.puzzles);
    }
    private Set<WatchListener> listeners = new HashSet<>();
    /** A watch listener for the board  */
    public interface WatchListener {
        /** 
         * Called when the available matches in the game changes.
         * A change is defined as when a new match becomes available or an available match becomes full
         * @return String of the available matches since last change
         */
        public String onChange();
    }
    /**
     * 
     * @param listener Adds a new listener
     */
    public synchronized void addWatchListener(WatchListener listener) {
        listeners.add(listener);
    }
    private void callListeners() throws IOException{
        for (WatchListener listener : listeners) {
            listener.onChange();
        }
    }
    
    /**
     * TODO
     * @return game with puzzle names and their representation separated by newlines
     */
    @Override
    public String toString() {
        StringBuilder gameString = new StringBuilder();
        for (String puzzleName : puzzles.keySet()) {
            Puzzle puzzle = puzzles.get(puzzleName);
            gameString.append(puzzleName + "\n" + puzzle.toString() + "\n\n");
        }

        // Remove the string minus the newline at the end
        return gameString.substring(0,  gameString.length()-1);
    }
}
