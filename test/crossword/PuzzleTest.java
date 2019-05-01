package crossword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class PuzzleTest {
    
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
    
    // Test covers whether simple puzzle is consistent
    @Test
    public void testConsistentSimplePuzzle() throws IOException, UnableToParseException {
        Puzzle puzzle = makeSimplePuzzle();
        assertTrue(puzzle.isConsistent(), "Expected simple puzzle to be consistent");
        Puzzle parsedMetamorphicConsistent = Puzzle.parseFromFile("puzzles/Metamorphic.puzzle");
        assertTrue(parsedMetamorphicConsistent.isConsistent(), "Expected puzzle to be consistent");
        Puzzle parsedReactionsConsistent = Puzzle.parseFromFile("puzzles/Reactions.puzzle");
        assertTrue(parsedReactionsConsistent.isConsistent(), "Expected puzzle to be consistent");
    }
    
    @Test
    public void testInconsistentSimplePuzzle() throws IOException, UnableToParseException {
        // Test with premade puzzle objects
        Puzzle simpleInconsistentPuzzle = makeSimpleInconsistentPuzzle();
        assertFalse(simpleInconsistentPuzzle.isConsistent(), "Expected simple inconsistent puzzle to be inconsistent");
        Puzzle simpleOverlapPuzzle = makeSimpleOverlapPuzzle();
        assertFalse(simpleOverlapPuzzle.isConsistent(), "Expected simple overlapping puzzle to be inconsistent");
        
        // Test with puzzles made by parser
        Puzzle parsedSimpleInconsistentPuzzle = Puzzle.parseFromFile("puzzles/inconsistent.puzzle");
        assertFalse(parsedSimpleInconsistentPuzzle.isConsistent(), "Expected parsed simple inconsistent puzzle to be inconsistent");
        Puzzle parsedSimpleOverlapPuzzle = Puzzle.parseFromFile("puzzles/overlap.puzzle");
        assertFalse(parsedSimpleOverlapPuzzle.isConsistent(), "Expected parsed simple overlapping puzzle to be inconsistent");
        Puzzle parsedMetamorphicInconsistent = Puzzle.parseFromFile("puzzles/MetamorphicInconsistent.puzzle");
        assertFalse(parsedMetamorphicInconsistent.isConsistent(), "Expected puzzle to be inconsistent");
        Puzzle parsedReactionsInconsistent = Puzzle.parseFromFile("puzzles/ReactionsInconsistent.puzzle");
        assertFalse(parsedReactionsInconsistent.isConsistent(), "Expected puzzle to be inconsistent");
    }
    
    // Covers parsing simple.puzzle
    @Test
    public void testParserSimple() throws IOException, UnableToParseException {
        Puzzle expected = makeSimplePuzzle();
        Puzzle result = Puzzle.parseFromFile("puzzles/simple.puzzle");
        assertEquals(expected, result);
    }
}
