package crossword;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class PuzzleParserTest {
    
    private static void assertParseFromFile(Puzzle expected, String filename) 
            throws IOException, UnableToParseException {
        Puzzle result = PuzzleParser.parseFromFile(filename);
        assertEquals(expected, result, "Parsed puzzle is different than expected");
    }
    
    // Covers parsing simple.puzzle
    @Test
    public void testParseSimplePuzzle() throws IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimplePuzzle();
        assertParseFromFile(expected, "puzzles/simple.puzzle");
    }
    
    // Covers parsing an inconsistent puzzle
    @Test
    public void testParseInconsistentSimplePuzzle() throws IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimpleInconsistentPuzzle();
        assertParseFromFile(expected, "puzzles/simpleInconsistent.puzzle");
    }
    
    // Covers parsing an overlapping puzzle
    @Test
    public void testParseOverlappingSimplePuzzle() throws  IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimpleOverlapPuzzle();
        assertParseFromFile(expected, "puzzles/simpleOverlap.puzzle");
    }
    
    // Covers parsing a puzzle with comments
    @Test
    public void testParsePuzzleWithComments() throws  IOException, UnableToParseException {
        Puzzle expected = PuzzleTest.makeSimplePuzzle();
        assertParseFromFile(expected, "puzzles/simple.puzzle");
    }
}
