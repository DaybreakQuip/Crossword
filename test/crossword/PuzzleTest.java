package crossword;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class PuzzleTest {
    // Partitions:
    //  Length of name = 0, 1, > 1
    //  Length of description = 0, 1, > 1
    //  Number of puzzle entries in entries = 0, 1, > 1
    //  Number of puzzle entries that overlap = 0, 1, > 1
    //  Number of puzzle entries that intersect = 0, 1, > 1
    //  Puzzle name contains numbers, letters, or non-alphanumeric characters
    
    /**
     * @return a simple puzzle for testing
     */
    public static Puzzle makeSimplePuzzle() {
        List<PuzzleEntry> entries = new ArrayList<PuzzleEntry>();
        entries.add(new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        Puzzle simplePuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return simplePuzzle;
    }
    
    /**
     * @return a simple inconsistent puzzle for testing
     */
    public static Puzzle makeSimpleInconsistentPuzzle() {
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
        Puzzle simpleInconsistentPuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return simpleInconsistentPuzzle;
    }
    
    /**
     * @return a simple overlapping puzzle for testing
     */
    public static Puzzle makeSimpleOverlapPuzzle() {
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
     * @return an empty puzzle for testing
     */
    public static Puzzle makeEmptyPuzzle() {
        List<PuzzleEntry> entries = new ArrayList<>();
        Puzzle emptyPuzzle = new Puzzle("", "", entries);
        
        return emptyPuzzle;
    }
    
    // This test covers:
    //  Length of name = 0
    //  Length of description = 0
    //  Number of puzzle entries in entries = 0
    //  Number of puzzle entries that overlap = 0
    //  Number of puzzle entries that intersect = 0
    //  Puzzle name contains no characters
    @Test
    public void testEmptyPuzzle() {
        Puzzle emptyPuzzle = makeEmptyPuzzle();
        assertEquals(emptyPuzzle.getDescription(), "");
        assertEquals(emptyPuzzle.getName(), "");
        assertTrue(emptyPuzzle.isConsistent());
        assertTrue(emptyPuzzle.getEntries().size() == 0);
    }
    
    // This test covers:
    //  Length of name > 1
    //  Length of description = 1
    //  Number of puzzle entries in entries = 1
    //  Number of puzzle entries that overlap = 0
    //  Number of puzzle entries that intersect = 0
    //  Puzzle name contains numbers and non-alphanumeric characters
    @Test
    public void testWeirdPuzzle() {
        List<PuzzleEntry> entries = new ArrayList<>();
        entries.add(new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0)));
        Puzzle weirdPuzzle = new Puzzle("E4$y!", "Ez", entries);

        assertEquals(weirdPuzzle.getDescription(), "Ez");
        assertEquals(weirdPuzzle.getName(), "E4$y!");
        assertTrue(weirdPuzzle.isConsistent());
        assertTrue(weirdPuzzle.getEntries().size() == 1);
    }
    
    // This test covers:
    //  Length of name = 1, > 1
    //  Length of description = 1, > 1
    //  Number of puzzle entries in entries = 1, > 1
    //  Number of puzzle entries that overlap = 0, 1, > 1
    //  Number of puzzle entries that intersect = 0, 1, > 1
    //  Puzzle name contains letters
    @Test
    public void testConsistentSimplePuzzle() throws IOException, UnableToParseException {
        // Test with premade puzzle objects
        Puzzle puzzle = makeSimplePuzzle();
        assertTrue(puzzle.isConsistent(), "Expected simple puzzle to be consistent");
        
        // Test with puzzles made by parser
        Puzzle parsedPuzzle = Puzzle.parseFromFile("puzzles/simple.puzzle");
        assertTrue(parsedPuzzle.isConsistent(), "Expected parsed simple puzzle to be consistent");
        Puzzle parsedMetamorphicConsistent = Puzzle.parseFromFile("puzzles/metamorphic.puzzle");
        assertTrue(parsedMetamorphicConsistent.isConsistent(), "Expected puzzle to be consistent");
        Puzzle parsedReactionsConsistent = Puzzle.parseFromFile("puzzles/reactions.puzzle");
        assertTrue(parsedReactionsConsistent.isConsistent(), "Expected puzzle to be consistent");
    }
    
    // This test covers:
    //  Length of name = 1, > 1
    //  Length of description = 1, > 1
    //  Number of puzzle entries in entries = 1, > 1
    //  Number of puzzle entries that overlap = 0, 1, > 1
    //  Number of puzzle entries that intersect = 0, 1, > 1
    //  Puzzle name contains letters
    @Test
    public void testInconsistentSimplePuzzle() throws IOException, UnableToParseException {
        // Test with premade puzzle objects
        Puzzle simpleInconsistentPuzzle = makeSimpleInconsistentPuzzle();
        assertFalse(simpleInconsistentPuzzle.isConsistent(), "Expected simple inconsistent puzzle to be inconsistent");
        Puzzle simpleOverlapPuzzle = makeSimpleOverlapPuzzle();
        assertFalse(simpleOverlapPuzzle.isConsistent(), "Expected simple overlapping puzzle to be inconsistent");
        
        // Test with puzzles made by parser
        Puzzle parsedSimpleInconsistentPuzzle = Puzzle.parseFromFile("puzzles/simpleInconsistent.puzzle");
        assertFalse(parsedSimpleInconsistentPuzzle.isConsistent(), "Expected parsed simple inconsistent puzzle to be inconsistent");
        Puzzle parsedSimpleOverlapPuzzle = Puzzle.parseFromFile("puzzles/simpleOverlap.puzzle");
        assertFalse(parsedSimpleOverlapPuzzle.isConsistent(), "Expected parsed simple overlapping puzzle to be inconsistent");
        Puzzle parsedMetamorphicInconsistent = Puzzle.parseFromFile("puzzles/metamorphicInconsistent.puzzle");
        assertFalse(parsedMetamorphicInconsistent.isConsistent(), "Expected puzzle to be inconsistent");
        Puzzle parsedReactionsInconsistent = Puzzle.parseFromFile("puzzles/reactionsInconsistent.puzzle");
        assertFalse(parsedReactionsInconsistent.isConsistent(), "Expected puzzle to be inconsistent");
    }
}
