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
    
}
