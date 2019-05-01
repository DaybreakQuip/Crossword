package crossword;

/**
 * Immutable class representing a single crossword puzzle entry
 *
 */
public class PuzzleEntry {
    private final String word;
    private final String hint;
    private final Orientation orientation;
    private final Point position;
    
    /**
     * Create a new PuzzleEntry object 
     * @param word the word of this entry
     * @param position the position of this entry
     * @param hint the hint for the word
     * @param orientation the orientation of this entry
     */
    public PuzzleEntry(String word, String hint, Orientation orientation, Point position) {
        this.word = word;
        this.position = position;
        this.hint = hint;
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
     * @return this entry's hint
     */
    public String getHint() {
        return hint;
    }
    
    /**
     * @return this entry's orientation
     */
    public Orientation getOrientation() {
        return orientation;
    }
    
    @Override
    public int hashCode() {
        return word.length() + hint.length();
    }
    
    @Override
    public boolean equals(Object that) {
        if (!(that instanceof PuzzleEntry)) return false;
        else {
            PuzzleEntry other = (PuzzleEntry) that;
            return this.word.equals(other.word) 
                    && this.hint.equals(other.hint) 
                    && this.orientation == other.orientation
                    && this.position.equals(other.position);
        }
    }
    
    @Override
    public String toString() {
        return "(" + word + ", " + hint + ", " + orientation + ", " + position + ")";
    }
}
