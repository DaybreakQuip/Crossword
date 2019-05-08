package crossword;

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
    //  entries is only accessed through getter methods like getEntries which creates a copy before returning to the client
    // Thread safety argument:
    //  TODO: Monitor Pattern
    private final String name; 
    private final String description;
    private final Map<Integer, PuzzleEntry> playerEntries = new HashMap<>();
    private final Map<Integer, PuzzleEntry> confirmedEntries  = new HashMap<>();
    private final Map<Integer, PuzzleEntry> correctEntries;
    /**
     * Constructs a puzzle that can be played by different players
     * @param puzzle an unmodifiable puzzle with the correct answers
     */
    public PlayablePuzzle(Puzzle puzzle) {
        name = puzzle.getName();
        description = puzzle.getDescription();
        correctEntries = puzzle.getEntries();
    }
    
    /**
     * Adds a player entry/guess to a map of words player guessed
     * @param wordID the id of the word on the crossword puzzle
     * @param word word to add as a puzzle entry 
     * @return true if word is added, false otherwise
     */
    public boolean addPlayerEntry(int wordID, PuzzleEntry word) {
        throw new RuntimeException("Not Implemented");
    }
    /**
     * Deletes the entry of the word from the playerEntries
     * @param wordID the id of the word on the crossword puzzle
     * @return true if entry is deleted, false otherwise
     */
    public boolean deletePlayerEntry(int wordID) {
        throw new RuntimeException("Not Implemented");
    }
    
    /**
     * Adds a confirmed entry/guess to a map of words that has been confirmed
     * @param wordID the id of the word on the crossword puzzle
     * @param word word to add as a puzzle entry 
     * @return true if word is added, false otherwise
     */
    public boolean addConfirmedEntry(int wordID, PuzzleEntry word) {
        throw new RuntimeException("Not Implemented");
    }
    /**
     * Deletes the entry of the word from the confirmedEntry
     * @param wordID the id of the word on the crossword puzzle
     * @return true if entry is deleted, false otherwise

     */
    public boolean deleteConfirmedEntry(int wordID) {
        throw new RuntimeException("Not Implemented");
    }
}
