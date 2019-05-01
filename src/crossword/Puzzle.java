package crossword;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * Immutable Puzzle class representing a puzzle object
 *
 */
public class Puzzle {
    private final String name; 
    private final String description;
    private final Set<PuzzleEntry> entries;
    
    /**
     * @return a dummy puzzle for testing
     */
    public static Puzzle makeDummyPuzzle() {
        Set<PuzzleEntry> entries = new HashSet<PuzzleEntry>();
        entries.add(new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        //entries.add(new PuzzleEntry("moo", "cows", Orientation.ACROSS, new Point(0, 2)));
        Puzzle dummyPuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return dummyPuzzle;
    }
    
    /**
     * @return an inconsistent dummy puzzle for testing
     */
    public static Puzzle makeInconsistentPuzzle() {
        Set<PuzzleEntry> entries = new HashSet<PuzzleEntry>();
        entries.add(new PuzzleEntry("cat", "twinkle twinkle", Orientation.ACROSS, new Point(0, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        //entries.add(new PuzzleEntry("moo", "cows", Orientation.ACROSS, new Point(0, 2)));
        Puzzle dummyPuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return dummyPuzzle;
    }
    
    /**
     * Creates a new crossword Puzzle with pre-existing entries
     * @param name the name of the puzzle
     * @param description the description of the puzzle
     * @param entries the entries of the puzzle
     */
    public Puzzle(String name, String description, Set<PuzzleEntry> entries) {
        this.name = name;
        this.description = description;
        this.entries = Collections.unmodifiableSet(new HashSet<>(entries)); // Make a defensive, unmodifiable copy of the entries
    }
    
    /**
     * @return set of entries in the puzzle
     */
    public Set<PuzzleEntry> getEntries() {
        return new HashSet<>(entries);
    }
    
    /**
     * @return name of the puzzle
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return true if the puzzle is consistent and false otherwise
     */
    public boolean isConsistent() {
        return true;
    }
    
    @Override
    public String toString() {
        StringBuilder puzzleString = new StringBuilder();
        for (PuzzleEntry entry : entries) {
            puzzleString.append(entry.toString() + "\n");
        }
        // Remove the string minus the newline at the end
        return puzzleString.substring(0,  puzzleString.length()-1);
    }
}
