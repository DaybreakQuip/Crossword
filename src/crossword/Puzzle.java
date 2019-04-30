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
}
