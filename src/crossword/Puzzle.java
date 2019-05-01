package crossword;

import java.io.BufferedReader;
import java.io.FileReader;
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
    private final String name; 
    private final String description;
    private final List<PuzzleEntry> entries;
    
    /**
     * @return a dummy puzzle for testing
     */
    public static Puzzle makeDummyPuzzle() {
        List<PuzzleEntry> entries = new ArrayList<>();
        entries.add(new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        //entries.add(new PuzzleEntry("moo", "cows", Orientation.ACROSS, new Point(0, 2)));
        Puzzle simplePuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return simplePuzzle;
    }
    
    /**
     * @return an inconsistent dummy puzzle for testing
     */
    public static Puzzle makeInconsistentPuzzle() {
        List<PuzzleEntry> entries = new ArrayList<>();
        entries.add(new PuzzleEntry("cat", "twinkle twinkle", Orientation.ACROSS, new Point(0, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        //entries.add(new PuzzleEntry("moo", "cows", Orientation.ACROSS, new Point(0, 2)));
        Puzzle simplePuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return simplePuzzle;
    }
    
    /**
     * @return an inconsistent dummy puzzle for testing 
     */
    public static Puzzle makeInconsistentPuzzleOverlap() {
        List<PuzzleEntry> entries = new ArrayList<>();
        entries.add(new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        entries.add(new PuzzleEntry("rye", "Bread", Orientation.ACROSS, new Point(1, 3)));
        Puzzle simplePuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return simplePuzzle;
    }
    
    /**
     * Returns a new Puzzle by parsing a file
     * @param filename the name of the .puzzle file
     * @return a new Puzzle parsed from the file
     * @throws IOException if there is a problem with reading the file
     * @throws UnableToParseException if there is a problem with parsing
     */
    public static Puzzle parseFromFile(String filename) throws IOException, UnableToParseException{
        BufferedReader reader = new BufferedReader(new FileReader(filename));
        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            inputBuilder.append(line + "\n");
        }
        
        reader.close();
        String input = inputBuilder.toString();
        return PuzzleParser.parse(input);
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
        this.entries = Collections.unmodifiableList(new ArrayList<>(entries)); // Make a defensive, unmodifiable copy of the entries
    }
    
    /**
     * @return set of entries in the puzzle
     */
    public List<PuzzleEntry> getEntries() {
        return new ArrayList<>(entries);
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
                    System.out.println("Unique");
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
                            System.out.println("First overlap");
                            return false;
                        }
                    } else {
                        if (currentPosition.getCol() == otherPosition.getCol() &&
                                !(currentPosition.getRow() + currentWord.length() < otherPosition.getRow()
                                || currentPosition.getRow() > otherPosition.getRow() + otherWord.length())) {
                            System.out.println("Second overlap");
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
    
    @Override
    public String toString() {
        StringBuilder puzzleString = new StringBuilder();
        puzzleString.append("Name: " + name + "\n");
        puzzleString.append("Description: " + description + "\n");
        for (PuzzleEntry entry : entries) {
            puzzleString.append(entry.toString() + "\n");
        }
        // Remove the string minus the newline at the end
        return puzzleString.substring(0,  puzzleString.length()-1);
    }
}
