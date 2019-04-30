package crossword;

/**
 * Immutable class representing a single point in the puzzle
 */
public class Point {
    private final int row;
    private final int col;
    
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
}
