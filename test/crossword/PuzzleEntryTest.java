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
        
    }
}
