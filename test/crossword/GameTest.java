package crossword;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class GameTest {
    public static Puzzle makeDummyPuzzle() {
        List<PuzzleEntry> entries = new ArrayList<PuzzleEntry>();
        entries.add(new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0)));
        entries.add(new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2)));
        entries.add(new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2)));
        entries.add(new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5)));
        entries.add(new PuzzleEntry("bee", "Everyone loves honey", Orientation.ACROSS, new Point(4, 0)));
        entries.add(new PuzzleEntry("treasure", "Every pirate's dream", Orientation.ACROSS, new Point(5, 2)));
        entries.add(new PuzzleEntry("troll", "Everyone's favorite twitter pastime", Orientation.ACROSS, new Point(4, 4)));
        entries.add(new PuzzleEntry("loss", "This is not a gain", Orientation.DOWN, new Point(3, 6)));
        Puzzle dummyPuzzle = new Puzzle("Easy", "An easy puzzle to get started", entries);
        
        return dummyPuzzle;
    }
    public static Game createDummyGame() {
        Puzzle dummyPuzzle = makeDummyPuzzle();
        // then put the puzzle in a map
        Map<String, Puzzle> puzzles = new HashMap<>();
        puzzles.put(dummyPuzzle.getName(), dummyPuzzle);
        // lastly, initialize and a return a game with that map 
        return new Game(puzzles);
    }
    
    @Test
    public void testSimpleGame() {
        Game test = createDummyGame();
        assertEquals("4`twinkle twinkle`ACROSS`1`0~6`Farmers ______`DOWN`0`2~6`It's tea time!`"
                + "ACROSS`3`2~5`more`DOWN`1`5~3`Everyone loves honey`ACROSS`4`0~8`Every pirate's dream"
                + "`ACROSS`5`2~5`Everyone's favorite twitter pastime`ACROSS`4`4~4`This is not a gain`DOWN`3`6", test.getPuzzleForResponse("Easy"), "not correct Puzzle entries");
    }
}
