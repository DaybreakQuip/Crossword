package crossword;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class GameTest {
    
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
        assertEquals("4`twinkle twinkle`ACROSS`1`0~6`Farmers ______`DOWN`0`2~6`It's tea time!`"
                + "ACROSS`3`2~5`more`DOWN`1`5~3`Everyone loves honey`ACROSS`4`0~8`Every pirate's dream"
                + "`ACROSS`5`2~5`Everyone's favorite twitter pastime`ACROSS`4`4~4`This is not a gain`DOWN`3`6", test.getPuzzleForResponse("Easy"), "not correct Puzzle entries");
    }
}
