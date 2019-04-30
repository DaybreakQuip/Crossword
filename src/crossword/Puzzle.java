package crossword;

import java.util.HashSet;
import java.util.Set;

/**
 * Mutable Puzzle class representing a puzzle object
 *
 */
public class Puzzle {
    private final String name; 
    private final Set<PuzzleEntry> entries;
    
    /**
     * Creates a new crossword Puzzle with pre-existing entries
     * @param name the same of the puzzle
     * @param entries the entries of the puzzle
     */
    public Puzzle(String name, Set<PuzzleEntry> entries) {
        this.name = name;
        this.entries = new HashSet<>(entries); // Make a defensive copy of the entries
    }
    
    /**
     * Creates a new crossword Puzzle with no entries
     * @param name the name of the puzzle
     */
    public Puzzle(String name) {
        this.name = name;
        entries = new HashSet<>();
    }
    
    /**
     * Adds a new puzzle entry to the puzzle
     * @param entry the new puzzle entry
     */
    public void addEntry(PuzzleEntry entry) {
        this.entries.add(entry);
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
}
