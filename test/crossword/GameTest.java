package crossword;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.jupiter.api.Test;

import edu.mit.eecs.parserlib.UnableToParseException;

public class GameTest {
    //Partitions
    //Number of matches in the game = 0, 1, > 1
    //Number of players in the game = 0, 1, > 1
    //Number of puzzles in the game = > 1
    //Whether a player joins a match
    //Whether a player creates a match
    //Whether a play quits a game

    
    public static Game createSimpleGame() {
        // Make a simple puzzle
        Puzzle simplePuzzle = PuzzleTest.makeSimplePuzzle();
        // then put the puzzle in a map
        Map<String, Puzzle> puzzles = new HashMap<>();
        puzzles.put(simplePuzzle.getName(), simplePuzzle);
        // lastly, initialize and a return a game with that map 
        return new Game(puzzles);
    }
    
    // Test covers getPuzzleForResponse
    @Test
    public void testSimpleGame() {
        Game test = createSimpleGame();
        assertEquals("0 bs1fc 4 bs1fc twinkle twinkle bs1fc ACROSS bs1fc 1 bs1fc 0 as3fb "
                + "1 bs1fc 6 bs1fc Farmers ______ bs1fc DOWN bs1fc 0 bs1fc 2 as3fb "
                + "2 bs1fc 6 bs1fc It's tea time! bs1fc ACROSS bs1fc 3 bs1fc 2 as3fb "
                + "3 bs1fc 5 bs1fc more bs1fc DOWN bs1fc 1 bs1fc 5 as3fb "
                + "4 bs1fc 3 bs1fc Everyone loves honey bs1fc ACROSS bs1fc 4 bs1fc 0 as3fb "
                + "5 bs1fc 8 bs1fc Every pirate's dream bs1fc ACROSS bs1fc 5 bs1fc 2 as3fb "
                + "6 bs1fc 5 bs1fc Everyone's favorite twitter pastime bs1fc ACROSS bs1fc 4 bs1fc 4 as3fb "
                + "7 bs1fc 4 bs1fc This is not a gain bs1fc DOWN bs1fc 3 bs1fc 6", 
                test.getPuzzleForResponse("Easy"), "not correct Puzzle entries");
    }
    
    public void addPlayerListeners(Game test, String playerOne, String playerTwo) {
        test.addPlayListener(playerOne, new Game.PlayListener() {
            public void onChange() {
            }
        });
        test.addPlayListener(playerTwo, new Game.PlayListener() {
            public void onChange() {
            }
        });
    }
    //More than One match in the game, player joins match, player creates match, >1 player in a game, test one player in game, test two player in game
    @Test
    public void testComplicatedGame() throws UnableToParseException, IOException {
        Game test = Game.parseGameFromFiles("puzzles/");
        String[] puz = {"6031", "Easy", "SimpleComments", "Cross", "Easy1", "Reactions", "Metamorphic"};
        Set<String> puzzles = new HashSet<String>(Arrays.asList(puz));
        assertEquals(puzzles,test.getPuzzleNames());
        assertTrue(test.login("me"));
        assertFalse(test.login("me"));
        assertTrue(test.login("you"));
        assertFalse(test.login("you"));
        assertTrue(test.login("nome"));
        assertFalse(test.login("nome"));
        assertTrue(test.login("noyou"));
        assertFalse(test.login("noyou"));
        test.addWaitListener("me", new Game.WaitListener() {
            public void onChange() {
            }
        });
        test.addWaitListener("you", new Game.WaitListener() {
            public void onChange() {                
            }
        });
        test.addWaitListener("nome", new Game.WaitListener() {
            public void onChange() {
            }
        });
        test.addWaitListener("noyou", new Game.WaitListener() {
            public void onChange() {                
            }
        });
        assertTrue(test.createMatch("me", "0", "Reactions", "Having Fun"));
        assertFalse(test.createMatch("you", "0", "Reactions", "Havingf Fun"));
        assertTrue(test.createMatch("nome", "1", "Cross", "Fun Stuff"));
        assertFalse(test.createMatch("nome", "1", "Cross", "recreation"));
        assertEquals("0 bs1fc Having Fun as3fb 1 bs1fc Fun Stuff", test.getAvailableMatchesForResponse());
        assertTrue(test.joinMatch("you", "0"));
        test.addPlayListener("me", new Game.PlayListener() {
            public void onChange() {
            }
        });
        assertThrows(RuntimeException.class, ()->{test.tryWord("me", 0, "products");});
        test.addPlayListener("me", new Game.PlayListener() {
            public void onChange() {
            }
        });
        assertThrows(RuntimeException.class, ()->{test.tryWord("me", 12, "doommgeys");});
        test.addPlayListener("me", new Game.PlayListener() {
            public void onChange() {
            }
        });
        assertEquals("1 bs1fc Fun Stuff", test.getAvailableMatchesForResponse());
        assertTrue(test.joinMatch("noyou", "1"));
        assertEquals("", test.getAvailableMatchesForResponse());
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 0, "products"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 1, "ababafffbaba"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("you", 0, "products"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 2, "enzyems"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 12, "doommgeys"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("me", 2, "enzymes"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.challengeWord("you", 12, "decreases"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 10, "increasing"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 1, "catalysts"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 12, "decreases"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("me", 1, "catablots"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 13, "activation"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me",14, "lechatelet"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 14, "le chatelier"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 14, "lechatelier"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 4, "exothermic"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("me", 4, "octivation"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("you", 14, "lechateliee"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.challengeWord("you", 15, "balanced"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 8, "areas"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 5, "aaaaaaaaaa"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 5, "aaaaaaaaaa"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 5, "aaaaaaaaa"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 6, "reversible"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 11, "equal"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 9, "energy"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("you", 5, "reactants"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 7, "dynamic"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 15, "goesfaster"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 10, "increased"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 12, "decreased"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 3, "collide"));
        addPlayerListeners(test, "me","you");
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 8 as3fb you bs1fc 1 bs1fc 9 cs2fd you wins!", test.showScore("me"));
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 8 as3fb you bs1fc 1 bs1fc 9 cs2fd you wins!", test.showScore("you"));
        addPlayerListeners(test, "nome","noyou");
        assertTrue(test.tryWord("nome", 0, "mat"));
        addPlayerListeners(test, "nome","noyou");
        assertFalse(test.tryWord("noyou", 0, "cat"));
        addPlayerListeners(test, "nome","noyou");
        assertTrue(test.challengeWord("noyou", 0, "cat"));
        addPlayerListeners(test, "nome","noyou");
        assertFalse(test.challengeWord("noyou", 1, "mat"));
        addPlayerListeners(test, "nome","noyou");
        assertTrue(test.tryWord("nome", 1, "mat"));
        addPlayerListeners(test, "nome","noyou");
        assertEquals("DONE cs2fd nome bs1fc 0 bs1fc 1 as3fb noyou bs1fc 2 bs1fc 3 cs2fd noyou wins!", test.showScore("nome"));
        assertEquals("DONE cs2fd nome bs1fc 0 bs1fc 1 as3fb noyou bs1fc 2 bs1fc 3 cs2fd noyou wins!", test.showScore("noyou"));
    }
    
    //Tests exiting game and logging out
    @Test
    public void exitlogOut() throws UnableToParseException, IOException {
        Game test = Game.parseGameFromFiles("puzzles/");
        String[] puz = {"6031", "Easy", "SimpleComments", "Cross", "Easy1", "Reactions", "Metamorphic"};
        Set<String> puzzles = new HashSet<String>(Arrays.asList(puz));
        assertEquals(puzzles,test.getPuzzleNames());
        assertTrue(test.login("me"));
        assertFalse(test.login("me"));
        assertTrue(test.login("you"));
        assertFalse(test.login("you"));
        test.addWaitListener("me", new Game.WaitListener() {
            public void onChange() {
            }
        });
        test.addWaitListener("you", new Game.WaitListener() {
            public void onChange() {                
            }
        });
        test.addWaitListener("nome", new Game.WaitListener() {
            public void onChange() {
            }
        });
        test.addWaitListener("noyou", new Game.WaitListener() {
            public void onChange() {                
            }
        });
        assertTrue(test.createMatch("me", "0", "Reactions", "Having Fun"));
        assertFalse(test.createMatch("you", "0", "Reactions", "Havingf Fun"));
        assertTrue(test.createMatch("nome", "1", "Cross", "Fun Stuff"));
        assertFalse(test.createMatch("nome", "1", "Cross", "recreation"));
        test.exitWait("nome");
        assertEquals("0 bs1fc Having Fun", test.getAvailableMatchesForResponse());
        assertTrue(test.joinMatch("you", "0"));
        assertEquals("", test.getAvailableMatchesForResponse());
        assertFalse(test.joinMatch("noyou", "1"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 0, "products"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 1, "ababafffbaba"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("you", 0, "products"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 2, "enzyems"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 12, "doommgeys"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("me", 2, "enzymes"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.challengeWord("you", 12, "decreases"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 10, "increasing"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 1, "catalysts"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 12, "decreases"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("me", 1, "catablots"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 13, "activation"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me",14, "lechatelet"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 14, "le chatelier"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("me", 14, "lechatelier"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 4, "exothermic"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("me", 4, "octivation"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.challengeWord("you", 14, "lechateliee"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.challengeWord("you", 15, "balanced"));
        addPlayerListeners(test, "me","you");
        assertTrue(test.tryWord("you", 8, "areas"));
        addPlayerListeners(test, "me","you");
        test.exitPlay("me");
        assertFalse(test.tryWord("me", 5, "aaaaaaaaaa"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 5, "aaaaaaaaaa"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 5, "aaaaaaaaa"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("you", 6, "reversible"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 11, "equal"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("you", 9, "energy"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.challengeWord("you", 5, "reactants"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("you", 7, "dynamic"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 15, "goesfaster"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 10, "increased"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 12, "decreased"));
        addPlayerListeners(test, "me","you");
        assertFalse(test.tryWord("me", 3, "collide"));
        addPlayerListeners(test, "me","you");
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 4 as3fb you bs1fc -1 bs1fc 3 cs2fd me wins!", test.showScore("me"));
        assertEquals("DONE cs2fd me bs1fc 0 bs1fc 4 as3fb you bs1fc -1 bs1fc 3 cs2fd me wins!", test.showScore("you"));
        assertFalse(test.logout("me"));
        assertFalse(test.logout("you"));
        assertFalse(test.logout("who"));
        assertFalse(test.logout("nome"));
        assertFalse(test.logout("noyou"));
        assertFalse(test.login("me"));
        assertFalse(test.login("you"));
    }
}
