package crossword;

/**
 * Immutable class representing a single point in the puzzle
 */
public class Point {
    private final int row;
    private final int col;
    
    // Abstraction function:
    //    AF(row, col) = a point representing a location on the crossword board located at ith row and jth column where i = row and j = col
    // Representation invariant:
    //  true
    // Safety from rep exposure:
    //  all fields are final, private, and immutable
    // Thread safety argument:
    //  This class is immutable
      
    /**
     * Create a new point representing location at (row, col)
     * @param row the row of the point
     * @param col the column of the point
     */
    public Point(int row, int col) {
        this.row = row;
        this.col = col;
    }
    
    /**
     * @return row of the point
     */
    public int getRow() {
        return row;
    }
    
    /**
     * @return col of the point
     */
    public int getCol() {
        return col;
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof Point && sameValue((Point) other);
    }
    
    /**
     * @param other point to compare to
     * @return true of this and other point are equal and false otherwise
     */
    public boolean sameValue(Point other) {
        return this.row == other.row && this.col == other.col;
    }
    
    @Override
    public int hashCode() {
        return this.row + this.col;
    }
    
    @Override
    public String toString() {
        return "(" + row + ", " + col + ")";
    }
}
