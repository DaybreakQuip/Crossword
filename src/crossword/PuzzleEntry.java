package crossword;

/**
 * Immutable class representing a single crossword puzzle entry
 *
 */
public class PuzzleEntry {
    private final String word;
    private final String clue;
    private final Orientation orientation;
    private final Point position;
    
    // Abstraction function:
    //  AF(word, clue, orientation, position): word entry in the crossword puzzle oriented horizontally 
    //                                         if orientation == Orientation.ACROSS and vertically if
    //                                         orientation == Orientation.DOWN, starting at row position.getRow()
    //                                         and column position.getCol() in the crossword puzzle. 
    //                                         Clue is a hint with respect to word.
    // Representation invariant:
    //  true
    // Safety from rep exposure:
    //  All fields are private, final, and immutable
    // Thread safety argument:
    //  This class is immutable and therefore threadsafe
    
    /**
     * Create a new PuzzleEntry object 
     * @param word the word of this entry
     * @param position the position of this entry
     * @param clue the clue for the word
     * @param orientation the orientation of this entry
     */
    public PuzzleEntry(String word, String clue, Orientation orientation, Point position) {
        this.word = word;
        this.position = position;
        this.clue = clue;
        this.orientation = orientation;
    }
    
    /**
     * @return this entry's word
     */
    public String getWord() {
        return word;
    }
    
    /**
     * @return this entry's position
     */
    public Point getPosition() {
        return position;
    }
    
    /**
     * @return this entry's clue
     */
    public String getClue() {
        return clue;
    }
    
    /**
     * @return this entry's orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }
    
    @Override
    public int hashCode() {
        return word.hashCode() + clue.hashCode() + orientation.hashCode() + position.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof PuzzleEntry && sameValue((PuzzleEntry) other);
    }

    /**
     * @param other the other puzzle entry to compare to
     * @return true if this and other puzzle entry are equal and false otherwise
     */
    public boolean sameValue(PuzzleEntry other) {
        return word.equals(other.word) && clue.equals(other.clue) 
                && orientation == other.orientation && position.equals(other.position);
    }
    
    /**
     * @return puzzle entry in the form (word, clue, orientation, position)
     */
    @Override
    public String toString() {
        return "(" + word + ", " + clue + ", " + orientation + ", " + position + ")";
    }
}
