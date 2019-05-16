package crossword;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

public class PuzzleEntryTest {
    // Partitions:
    //     Length of word = 0, 1, > 1
    //     Length of clue = 0, 1, > 1
    //     Orientation of puzzle entry = ACROSS, DOWN
    //     Position of the puzzle entry:
    //         Row number = 0, 1, > 1
    //         Column number = 0, 1, > 1 

    // Covers: length of word = 0, length of clue = 0, orientation = ACROSS, row = 0, column = 0
    @Test
    public void testEmptyEntry() {
        PuzzleEntry puzzleEntry = new PuzzleEntry("", "", Orientation.ACROSS, new Point(0,0));
        assertEquals(puzzleEntry, new PuzzleEntry("", "", Orientation.ACROSS, new Point(0,0)));
    }
    
    // Covers: length of word = 1, length of clue > 1, orientation = DOWN, row > 1, column = 1
    @Test
    public void testSimpleEntry() {
        PuzzleEntry puzzleEntry = new PuzzleEntry("b", "bee", Orientation.DOWN, new Point(51,1));
        assertEquals(puzzleEntry, new PuzzleEntry("b", "bee", Orientation.DOWN, new Point(51,1)));
    }
    
    // Covers: length of word > 1, length of clue = 1, orientation = ACROSS, row = 1, column > 1
    @Test
    public void testAnotherSimpleEntry() {
        PuzzleEntry puzzleEntry = new PuzzleEntry("apple", "a", Orientation.ACROSS, new Point(1,34));
        assertEquals(puzzleEntry, new PuzzleEntry("apple", "a", Orientation.ACROSS, new Point(1,34)));
    }
}
