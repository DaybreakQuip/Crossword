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
        // first create the puzzle to be added
        Set<PuzzleEntry> entries = new HashSet<PuzzleEntry>();
        entries.add(new PuzzleEntry("cat", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        //entries.add(new PuzzleEntry("moo", "cows", Orientation.ACROSS, new Point(0, 2)));
        Puzzle dummyPuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
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
            puzzleString += entry.getWord().length() + ", " + entry.getHint() + ", " + 
        entry.getOrientation() + ", " + entry.getPosition().getRow() + ", " + entry.getPosition().getCol() + "\n";
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
