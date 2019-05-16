package crossword;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class PuzzleParserTest {
    
    // Partitions:
    //    Number of puzzle entries to parse = 0, 1, > 1
    //    Whether a puzzle contains overlap
    //    Whether a puzzle contains intersection
    //    Whether a puzzle contains comments
    //    Whether a puzzle contains words ACROSS
    //    Whether a puzzle contains words DOWN
    
    /*
     * Helper method to assert that a parsed puzzle is the same as an expected puzzle
     */
    private static void assertParseFromFile(Puzzle expected, String filename) 
            throws IOException, UnableToParseException {
        Puzzle result = PuzzleParser.parseFromFile(filename);
        assertEquals(expected, result, "Parsed puzzle is different than expected");
    }
    
    // This test covers:
    //    Number of puzzle entries to parse > 1
    //    Puzzle does not contain overlap
    //    Puzzle does not contain intersection
    //    Puzzle does not contain comments
    //    Puzzle contains words ACROSS
    //    Puzzle contains words DOWN
    @Test
    public void testParseSimplePuzzle() throws IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimplePuzzle();
        assertParseFromFile(expected, "puzzles/simple.puzzle");
    }
    
    // This test covers:
    //    Number of puzzle entries to parse > 1
    //    Puzzle does not contain overlap
    //    Puzzle contains intersection
    //    Puzzle does not contain comments
    //    Puzzle contains words ACROSS
    //    Puzzle contains words DOWN
    @Test
    public void testParseInconsistentSimplePuzzle() throws IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimpleInconsistentPuzzle();
        assertParseFromFile(expected, "puzzles/simpleInconsistent.puzzle");
    }
    
    // This test covers:
    //    Number of puzzle entries to parse > 1
    //    Puzzle contains overlap
    //    Puzzle does not contain intersection
    //    Puzzle does not contain comments
    //    Puzzle contains words ACROSS
    //    Puzzle contains words DOWN
    @Test
    public void testParseOverlappingSimplePuzzle() throws  IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimpleOverlapPuzzle();
        assertParseFromFile(expected, "puzzles/simpleOverlap.puzzle");
    }
    
    // This test covers:
    //    Number of puzzle entries to parse > 1
    //    Puzzle does not contain overlap
    //    Puzzle does not contain intersection
    //    Puzzle contains comments
    //    Puzzle contains words ACROSS
    //    Puzzle contains words DOWN
    @Test
    public void testParsePuzzleWithComments() throws  IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimplePuzzle();
        assertParseFromFile(expected, "puzzles/simple.puzzle");
    }
    // Manual test
    // This test covers:
    //    Number of puzzle entries to parse = 0
    //    Puzzle does not contain overlap
    //    Puzzle does not contain intersection
    //    Puzzle contains comments
    //    Puzzle does not contain words ACROSS
    //    Puzzle does not contain words DOWN
    // 1. Create a new empty.puzzle file with name = "Empty" and description = "An empty puzzle"
    // 2. Parse the file with PuzzleParser.parseFromFile(empty.puzzle) and assert that the
    //      parsed puzzle is equivalent to an empty puzzle with only a name and description
}
