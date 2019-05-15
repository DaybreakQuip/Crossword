package crossword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.mit.eecs.parserlib.UnableToParseException;

/**
 * Immutable Puzzle class representing a puzzle object
 *
 */
public class Puzzle {
    // Abstraction Function:
    // AF(name, description, entries): a Puzzle representing a crossword puzzle with a name 
    //                                 and a description for the crossword. There is a map of ID 
    //                                 to PuzzleEntries that represents each word in the puzzle.
    //                                    
    // Rep Invariant:
    // true
    // Safety From Rep Exposure:
    //  name and description is private and final and immutable
    //  entries is only accessed through getter methods like getEntries which creates a copy 
    //      before returning to the client
    // Thread safety argument:
    //  This class is immutable and therefore threadsafe
    private final String name; 
    private final String description;
    private final Map<Integer, PuzzleEntry> entries;
    
    /**
     * Returns a new Puzzle by parsing a file
     * @param filename the name of the .puzzle file
     * @return a new Puzzle parsed from the file
     * @throws IOException if there is a problem with reading the file
     * @throws UnableToParseException if there is a problem with parsing
     */
    public static Puzzle parseFromFile(String filename) throws IOException, UnableToParseException{
        return PuzzleParser.parseFromFile(filename);
    }
    
    /**
     * Creates a new crossword Puzzle with pre-existing entries
     * @param name the name of the puzzle
     * @param description the description of the puzzle
     * @param entries the entries of the puzzle
     */
    public Puzzle(String name, String description, List<PuzzleEntry> entries) {
        this.name = name;
        this.description = description;
        // Create the entries map for the puzzle that maps a unique id to each entry
        Map<Integer, PuzzleEntry> tempEntries = new HashMap<>();
        for (int i = 0; i < entries.size(); i++) {
            tempEntries.put(i,  entries.get(i));
        }
        this.entries = Collections.unmodifiableMap(tempEntries); // Wrap the entries with an unmodifiable map
    }
    
    /**
     * @return map of entries in the puzzle
     */
    public Map<Integer, PuzzleEntry> getEntries() {
        return entries;
    }
    
    /**
     * @return name of the puzzle
     */
    public String getName() {
        return name;
    }
    
    /**
     * @return description of the puzzle
     */
    public String getDescription() {
        return description;
    }
    
    /**
     * @return true if the puzzle is consistent and false otherwise
     */
    public boolean isConsistent() {
        // check whether intersections are constant 
        Map<Point, Character> pointToLetter = new HashMap<>();
        for (int i = 0; i < entries.size(); i++) {
            PuzzleEntry entry = entries.get(i);
            String word = entry.getWord();
            Orientation orientation = entry.getOrientation();
            Point position = entry.getPosition();
            for (int j = 0; j < word.length(); j++) {
                Point letterPosition; // Position of the letter in the word
                if (orientation == Orientation.ACROSS) {
                    // getCol() + j represents the column of the letter
                    letterPosition = new Point(position.getRow(), position.getCol() + j);  
                } else { 
                    // getRow() + j represents the row of the letter
                    letterPosition = new Point(position.getRow() + j, position.getCol());
                }
                
                // If the point is already in the map and the letters do not match, return false
                //  otherwise add the point and letter pair to the map
                if (pointToLetter.containsKey(letterPosition) && pointToLetter.get(letterPosition) != word.charAt(j)) {
                    return false;
                } else {
                    pointToLetter.put(letterPosition, word.charAt(j));
                }
            }
        }
        
        for (int i = 0; i < entries.size(); i++) {
            PuzzleEntry currentPuzzle = entries.get(i);
            for (int j = i + 1; j < entries.size(); j++) {
                PuzzleEntry otherPuzzle = entries.get(j);
                
                // words must be unique
                if (currentPuzzle.getWord().equals(otherPuzzle.getWord()) 
                        && !currentPuzzle.equals(otherPuzzle)) {
                    return false;
                }
                
                final Orientation currentOrientation = currentPuzzle.getOrientation();
                final Point currentPosition = currentPuzzle.getPosition();
                final Point otherPosition = otherPuzzle.getPosition();
                final String currentWord = currentPuzzle.getWord();
                final String otherWord = otherPuzzle.getWord();
                
                if (currentOrientation == otherPuzzle.getOrientation()) {
                    // Check for overlaps
                    if (currentOrientation == Orientation.ACROSS) {
                        if (currentPosition.getRow() == otherPosition.getRow() && 
                                !(currentPosition.getCol() + currentWord.length() < otherPosition.getCol()
                                || currentPosition.getCol() > otherPosition.getCol() + otherWord.length())) {
                            return false;
                        }
                    } else {
                        if (currentPosition.getCol() == otherPosition.getCol() &&
                                !(currentPosition.getRow() + currentWord.length() < otherPosition.getRow()
                                || currentPosition.getRow() > otherPosition.getRow() + otherWord.length())) {
                            return false;
                        }
                    
                    }
                }
            }
        }
        return true;
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof Puzzle && sameValue((Puzzle) other);
    }

    /**
     * @param other the other puzzle to compare to
     * @return true if this and other puzzle are equal and false otherwise
     */
    public boolean sameValue(Puzzle other) {
        return name.equals(other.name) && description.equals(other.description) && entries.equals(other.entries); 
    }
    
    @Override
    public int hashCode() {
        return name.hashCode() + description.hashCode() + entries.hashCode();
    }
    
    /**
     * @return puzzle with the name of the puzzle, followed by the puzzle's description and
     *         all puzzle entries in the form (id, word, clue, orientation, (row, column))
     *         where each puzzle entry is separated by a newline 
     *         (without the trailing newline at the very end)
     */
    @Override
    public String toString() {
        StringBuilder puzzleString = new StringBuilder();
        puzzleString.append("Name: " + name + "\n");
        puzzleString.append("Description: " + description + "\n");
        for (Map.Entry<Integer, PuzzleEntry> entry : entries.entrySet()) {
            int id = entry.getKey();
            PuzzleEntry puzzleEntry = entry.getValue();        
            puzzleString.append("(" + id + ", " + puzzleEntry.getWord() + ", " + puzzleEntry.getClue() 
                        + ", " + puzzleEntry.getOrientation() + ", " + puzzleEntry.getPosition() + ")\n");
        }
        // Remove the string minus the newline at the end
        return puzzleString.substring(0,  puzzleString.length()-1);
    }
}
