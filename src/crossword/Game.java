package crossword;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * TODO: Make this class mutable for future milestones (?)
 * An immutable Game class representing a single Crossword Game
 *
 */
public class Game {
    private final Map<String, Puzzle> puzzles; // map of name : puzzle
    private static final String ENTRY_DELIM = "~";
    private static final String WORD_DELIM = "`";
    
    /**
     * @return a new Game with all puzzles parsed from files inside puzzles/
     */
    public static Game parseGameFromFiles() {
        // TODO: Fix this function to actually parse from file
        // Do something
        
        return new Game(new HashMap<String, Puzzle>());
    }
    
    /**
     * Returns a game meant for testing purposes
     * @return a new dummy game with puzzle entries from simple.puzzle
     */
    public static Game createDummyGame() {
        Puzzle dummyPuzzle = Puzzle.makeDummyPuzzle();
        // then put the puzzle in a map
        Map<String, Puzzle> puzzles = new HashMap<>();
        puzzles.put(dummyPuzzle.getName(), dummyPuzzle);
        // lastly, initialize and a return a game with that map 
        return new Game(puzzles);
    }
    
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
     * @return string with format: length, hint, orientation, row, col\n
     *         e.g: "4, "twinkle twinkle", ACROSS, 0, 1\n"
     */
    public String getPuzzleForResponse(String name) {
        Puzzle puzzle = puzzles.get(name);
        String puzzleString = "";
        for (PuzzleEntry entry: puzzle.getEntries()) {
            puzzleString += entry.getWord().length() + WORD_DELIM + entry.getHint() + WORD_DELIM + 
                            entry.getOrientation() + WORD_DELIM + entry.getPosition().getRow() 
                            + WORD_DELIM + entry.getPosition().getCol() + ENTRY_DELIM;
        }
        return puzzleString.substring(0,puzzleString.length()-1);
    }
    
    /**
     * @return set of the names of all puzzles in the game
     */
    public Set<String> getPuzzleNames() {
        return puzzles.keySet();
    }
}
