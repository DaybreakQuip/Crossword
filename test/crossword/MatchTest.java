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
    
    
    //This test covers 2 player match on a small puzzle based on empty and partially filled puzzles to completion
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
    //This test covers 2 player match on a small puzzle based on empty and partially filled puzzles to forfeit

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
    //This test covers 2 player match based on empty and partially filled puzzles to completion
    @Test
    public void testBigMatch() throws IOException, UnableToParseException {
        Puzzle template = Puzzle.parseFromFile("puzzles/reactions.puzzle");
        Match tester = new Match("0", "this is a test", template, "me");
        assertTrue(tester.isWaiting());
        tester.joinMatch("you");
        assertTrue(tester.isOngoing());
        assertTrue(tester.tryWord("me", 0, "products"));
        assertFalse(tester.tryWord("me", 1, "ababafffbaba"));
        assertFalse(tester.tryWord("you", 0, "products"));
        assertTrue(tester.tryWord("you", 2, "enzyems"));
        assertTrue(tester.tryWord("me", 12, "doommgeys"));
        assertTrue(tester.challengeWord("me", 2, "enzymes"));
        assertFalse(tester.challengeWord("you", 12, "decreases"));
        assertFalse(tester.tryWord("me", 10, "increasing"));
        assertTrue(tester.tryWord("you", 1, "catalysts"));
        assertTrue(tester.tryWord("me", 12, "decreases"));
        assertTrue(tester.challengeWord("me", 1, "catablots"));
        assertTrue(tester.tryWord("you", 13, "activation"));
        assertFalse(tester.tryWord("me",14, "lechatelet"));
        assertFalse(tester.tryWord("me", 14, "le chatelier"));
        assertTrue(tester.tryWord("me", 14, "lechatelier"));
        assertTrue(tester.tryWord("you", 4, "exothermic"));
        assertTrue(tester.challengeWord("me", 4, "octivation"));
        assertTrue(tester.challengeWord("you", 14, "lechateliee"));
        assertFalse(tester.challengeWord("you", 15, "balanced"));
        assertTrue(tester.tryWord("you", 8, "areas"));
        assertFalse(tester.tryWord("me", 5, "aaaaaaaaaa"));
        assertFalse(tester.tryWord("me", 5, "aaaaaaaaaa"));
        assertTrue(tester.tryWord("me", 5, "aaaaaaaaa"));
        assertTrue(tester.tryWord("you", 6, "reversible"));
        assertTrue(tester.tryWord("me", 11, "equal"));
        assertTrue(tester.tryWord("you", 9, "energy"));
        assertTrue(tester.challengeWord("you", 5, "reactants"));
        assertTrue(tester.tryWord("you", 7, "dynamic"));
        assertTrue(tester.tryWord("me", 15, "goesfaster"));
        assertTrue(tester.tryWord("me", 10, "increased"));
        assertTrue(tester.tryWord("me", 12, "decreased"));
        assertTrue(tester.tryWord("me", 3, "collide"));
        assertTrue(tester.isDone());
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 8 as3fb you bs1fc 1 bs1fc 9 cs2fd you wins!", tester.showScore());
    }
    //This test covers 2 player match based on empty and partially filled puzzles to forfeit
    @Test
    public void testBigMatchForfeit() throws IOException, UnableToParseException {
        Puzzle template = Puzzle.parseFromFile("puzzles/reactions.puzzle");
        Match tester = new Match("0", "this is a test", template, "me");
        assertTrue(tester.isWaiting());
        tester.joinMatch("you");
        assertTrue(tester.isOngoing());
        assertTrue(tester.tryWord("me", 0, "products"));
        assertFalse(tester.tryWord("me", 1, "ababafffbaba"));
        assertFalse(tester.tryWord("you", 0, "products"));
        assertTrue(tester.tryWord("you", 2, "enzyems"));
        assertTrue(tester.tryWord("me", 12, "doommgeys"));
        assertTrue(tester.challengeWord("me", 2, "enzymes"));
        assertFalse(tester.challengeWord("you", 12, "decreases"));
        assertFalse(tester.tryWord("me", 10, "increasing"));
        assertTrue(tester.tryWord("you", 1, "catalysts"));
        assertTrue(tester.tryWord("me", 12, "decreases"));
        assertTrue(tester.challengeWord("me", 1, "catablots"));
        assertTrue(tester.tryWord("you", 13, "activation"));
        assertFalse(tester.tryWord("me",14, "lechatelet"));
        assertFalse(tester.tryWord("me", 14, "le chatelier"));
        assertTrue(tester.tryWord("me", 14, "lechatelier"));
        assertTrue(tester.tryWord("you", 4, "exothermic"));
        assertTrue(tester.challengeWord("me", 4, "octivation"));
        assertTrue(tester.challengeWord("you", 14, "lechateliee"));
        assertFalse(tester.challengeWord("you", 15, "balanced"));
        assertTrue(tester.tryWord("you", 8, "areas"));
        assertFalse(tester.tryWord("me", 5, "aaaaaaaaaa"));
        assertFalse(tester.tryWord("me", 5, "aaaaaaaaaa"));
        assertTrue(tester.tryWord("me", 5, "aaaaaaaaa"));
        assertTrue(tester.tryWord("you", 6, "reversible"));
        assertTrue(tester.tryWord("me", 11, "equal"));
        assertTrue(tester.forfeit());
        assertTrue(tester.isDone());
        assertFalse(tester.forfeit());
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 6 as3fb you bs1fc -1 bs1fc 4 cs2fd me wins!", tester.showScore());
    }
    
    @Test
    public void forfeitRightAway() throws IOException, UnableToParseException {
        Puzzle template = Puzzle.parseFromFile("puzzles/reactions.puzzle");
        Match tester = new Match("0", "this is a test", template, "me");
        assertTrue(tester.isWaiting());
        tester.joinMatch("you");
        assertTrue(tester.isOngoing());
        assertTrue(tester.forfeit());
        assertTrue(tester.isDone());
        assertFalse(tester.forfeit());
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 0 as3fb you bs1fc 0 bs1fc 0 cs2fd Tie!", tester.showScore());

    }
}
