package crossword;

import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.HashMap;
import java.util.Map;

public class PlayablePuzzle {
    // Abstraction Function:
    // AF(name, description, playerEntries, confirmedEntries, correctEntries) --> A Puzzle representing a crossword puzzle with a name and a description for the crossword. 
    //                                   There is a map of word ID to PuzzleEntries that represents each word in the puzzle for player entries.
    //                                   There is a map of word ID to PuzzleEntries that represents each word in the puzzle for confirmed entries.
    //                                   There is a map of word ID to PuzzleEntries that represents each word in the puzzle for correct entries.
    //                                    
    // Rep Invariant:
    // true
    // Safety From Rep Exposure:
    //  name and description is private and final and immutable
    //  playerEntries, confirmedENtries, and correctEntries can only be gotten through getter methods, and returns a copy of the map, which
    //  only contains immutable objects
    //  playerEntries and confirmedEntries can only be modified through mutator methods
    //
    // Thread safety argument:
    //  Uses monitor Pattern
    private final String name; 
    private final String description;
    // TODO: Add player information (i.e. player 1 id and player 2 id or something)
    // New map of puzzle id : player OR player id : (list or set) puzzle id
    private final Map<Integer, SimpleImmutableEntry<Player, PuzzleEntry>> playerEntries = new HashMap<>(); // unconfirmed word id : puzzle entry
    private final Map<Integer, PuzzleEntry> confirmedEntries  = new HashMap<>();
    private final Map<Integer, PuzzleEntry> correctEntries;
    
    // TODO: DELETE this in the future (it's the template puzzle)
    private final Puzzle puzzle;
    /**
     * Constructs a puzzle that can be played by different players
     * @param puzzle an unmodifiable puzzle with the correct answers
     */
    public PlayablePuzzle(Puzzle puzzle) {
        name = puzzle.getName();
        description = puzzle.getDescription();
        correctEntries = puzzle.getEntries();
        this.puzzle = puzzle;
    }
    
    /**
     * Adds a player entry/guess to a map of words player guessed
     * @param wordID the id of the word on the crossword puzzle
     * @param word word to add as a puzzle entry 
     * @return true if word is added, false otherwise
     */
    public synchronized boolean addPlayerEntry(int wordID, Player player, PuzzleEntry word) {
        playerEntries.put(wordID, new SimpleImmutableEntry<>(player, word));
        return true;
    }
    /**
     * Deletes the entry of the word from the playerEntries
     * @param wordID the id of the word on the crossword puzzle
     * @return true if entry is deleted, false otherwise
     */
    public synchronized boolean deletePlayerEntry(int wordID) {
        if (!playerEntries.containsKey(wordID)) {
            return false;
        }
        playerEntries.remove(wordID);
        return true;
    }
    
    /**
     * Adds a confirmed entry/guess to a map of words that has been confirmed
     * @param wordID the id of the word on the crossword puzzle
     * @param word word to add as a puzzle entry 
     * @return true if word is added, false otherwise
     */
    public synchronized boolean addConfirmedEntry(int wordID, PuzzleEntry word) {
        if (confirmedEntries.containsKey(wordID)) {
            return false;
        }
        confirmedEntries.put(wordID, word);
        return true;
    }
    /**
     * Deletes the entry of the word from the confirmedEntry
     * @param wordID the id of the word on the crossword puzzle
     * @return true if entry is deleted, false otherwise

    public synchronized boolean deleteConfirmedEntry(int wordID) {
        throw new RuntimeException("Not Implemented");
    }*/
    
    /**
     * @return map of playerEntries in the puzzle
     */
    public synchronized Map<Integer, SimpleImmutableEntry<Player,PuzzleEntry>> getPlayerEntries() {
        return new HashMap<>(playerEntries);
    }
    
    /**
     * @return map of confirmedEntries in the puzzle
     */
    public synchronized Map<Integer, PuzzleEntry> getConfirmedEntries() {
        return new HashMap<>(confirmedEntries);
    }
    
    /**
     * @return map of correctEntries in the puzzle
     */
    public synchronized Map<Integer, PuzzleEntry> getCorrectEntries() {
        return new HashMap<>(correctEntries);
    }
    
    /**
     * Flattens playerEntries to <Integer, PuzzleEntry>
     * @return flattened player entries
     */
    public synchronized Map<Integer, PuzzleEntry> getFlattenedPlayerEntries()  {
        Map<Integer, PuzzleEntry> flattened = new HashMap<>();
        for (Map.Entry<Integer, SimpleImmutableEntry<Player,PuzzleEntry>> entry: playerEntries.entrySet()) {
            flattened.put(entry.getKey(), entry.getValue().getValue());
        }
        return flattened;
    }
    
    /**
     * @return puzzle formatted for responses
     */
    public synchronized String getPuzzleForResponse() {
        String puzzleString = "";
        for (Map.Entry<Integer, PuzzleEntry> entry: puzzle.getEntries().entrySet()) {
            Integer id = entry.getKey();
            PuzzleEntry puzzleEntry = entry.getValue();
            puzzleString += id + Game.WORD_DELIM + puzzleEntry.getWord().length() + Game.WORD_DELIM + puzzleEntry.getClue() + Game.WORD_DELIM + 
                    puzzleEntry.getOrientation() + Game.WORD_DELIM + puzzleEntry.getPosition().getRow() 
                            + Game.WORD_DELIM + puzzleEntry.getPosition().getCol() + Game.ENTRY_DELIM;
        }
        return puzzleString.substring(0,puzzleString.length()-1);
    }
    
    /**
     * 
     * @return player entries for responses
     */
    public synchronized String getGuessesForResponse() {
        String puzzleString = "";
        for (Map.Entry<Integer, SimpleImmutableEntry<Player,PuzzleEntry>> entry: playerEntries.entrySet()) {
            Integer wordID = entry.getKey();
            String playerID = entry.getValue().getKey().getId();
            PuzzleEntry puzzleEntry = entry.getValue().getValue();
            String confirmed = (confirmedEntries.containsKey(wordID)) ? "T" : "F";
            puzzleString += playerID + Game.WORD_DELIM + confirmed + Game.WORD_DELIM + wordID + Game.WORD_DELIM + puzzleEntry.getWord() + Game.WORD_DELIM + 
                    puzzleEntry.getOrientation() + Game.WORD_DELIM + puzzleEntry.getPosition().getRow() 
                            + Game.WORD_DELIM + puzzleEntry.getPosition().getCol() + Game.ENTRY_DELIM;
        }
        return puzzleString.substring(0,puzzleString.length()-1);
    }
    
    /**
     * @return name of the puzzle
     */
    public synchronized String getName() {
        return name;
    }
    
    /**
     * @return description of the puzzle
     */
    public synchronized String getDescription() {
        return description;
    }
}
