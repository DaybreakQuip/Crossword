package crossword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class MatchTest {
    // Partitions: 
    //     No player, one player, two players
    //     Number of puzzle entries (words) = 1, > 1
    //     Partially filled puzzle, empty puzzle
    //     Whether or not a player attempts a challenge
    //     Whether or not a player attempts a try
    //     Whether or not a player forfeits a match
    
    @Test
    public void testSimpleMatch() throws IOException, UnableToParseException {
        Puzzle template = Puzzle.parseFromFile("puzzles/cross.puzzle");
        Match tester = new Match("0", "this is a test", template, "me");
        assertTrue(tester.isWaiting());
        tester.joinMatch("you");
        assertTrue(tester.isOngoing());
        assertTrue(tester.tryWord("me", 0, "mat"));
        assertFalse(tester.tryWord("you", 0, "cat"));
        assertTrue(tester.challengeWord("you", 0, "cat"));
        assertFalse(tester.challengeWord("you", 1, "mat"));
        assertTrue(tester.tryWord("me", 1, "mat"));
        assertTrue(tester.isDone());
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 1 as3fb you bs1fc 2 bs1fc 3 cs2fd you wins!", tester.showScore());
    }
    @Test
    public void testForfeitMatch() throws IOException, UnableToParseException {
        Puzzle template = Puzzle.parseFromFile("puzzles/cross.puzzle");
        Match tester = new Match("0", "this is a test", template, "me");
        assertTrue(tester.isWaiting());
        tester.joinMatch("you");
        assertTrue(tester.isOngoing());
        assertTrue(tester.tryWord("me", 0, "mat"));
        assertFalse(tester.tryWord("you", 0, "cat"));
        assertTrue(tester.challengeWord("you", 0, "cat"));
        assertFalse(tester.challengeWord("you", 1, "mat"));
        assertTrue(tester.forfeit());
        assertTrue(tester.isDone());
        assertFalse(tester.forfeit());
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 0 as3fb you bs1fc 2 bs1fc 3 cs2fd you wins!", tester.showScore());
    }
}
