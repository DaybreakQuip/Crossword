package crossword;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;

public class PlayablePuzzleTest {
    // Partitions:
    //      Length of name = 0, 1, > 1
    //      Length of description = 0, 1, > 1
    //      Number of puzzle entries in playerEntries (entries that have been entered by players) = 0, 1, > 1
    //      Number of puzzle entries in confirmedEntries(entries that have been confirmed so far in the same match) = 0, 1, > 1
    //      Number of puzzle entries in correctEntries(the answer key for the puzzle for the match) = 0, 1, > 1
    //      Whether a player entry is added to the puzzle
    //      Whether a player entry is removed from the puzzle
    //      Whether a confirmed entry is added to the puzzle

    // Covers: length of name = 0, length of description = 0, number of puzzle entries in playerEntries = 0,
    //         number of puzzle entries in confirmedEntries = 0, number of puzzle entries in correctEntries = 0
    @Test
    public void testEmptyPlayable() {
        List<PuzzleEntry> entries = new ArrayList<PuzzleEntry>();
        Puzzle puzzle = new Puzzle("", "", entries);
        PlayablePuzzle playable = new PlayablePuzzle(puzzle);
        assertTrue(playable.getConfirmedEntries().keySet().size() == 0);
        assertTrue(playable.getCorrectEntries().keySet().size() == 0);
    }
    
    // Covers: length of name = 1, length of description = 1, number of puzzle entries in playerEntries = 1,
    //         number of puzzle entries in confirmedEntries = 1, number of puzzle entries in correctEntries = 1,
    //         player entry is added to the puzzle, confirmed entry is added to the puzzle
    @Test
    public void testAddAndConfirm() {
        List<PuzzleEntry> entries = new ArrayList<PuzzleEntry>();
        PuzzleEntry puzzleEntry = new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0));
        entries.add(puzzleEntry);
        Puzzle puzzle = new Puzzle("a", "b", entries);
        PlayablePuzzle playable = new PlayablePuzzle(puzzle);
        Player player = new Player("p");
        assertTrue(playable.addPlayerEntry(0, player, puzzleEntry));
        assertTrue(playable.addConfirmedEntry(0, puzzleEntry));
    }
    
    // Covers: length of name > 1, length of description > 1, number of puzzle entries in playerEntries > 1,
    //         number of puzzle entries in confirmedEntries > 1, number of puzzle entries in correctEntries > 1,
    //         player entry is removed from the puzzle
    @Test
    public void testDelete() {
        List<PuzzleEntry> entries = new ArrayList<PuzzleEntry>();
        PuzzleEntry puzzleEntryOne = new PuzzleEntry("star", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0));
        PuzzleEntry puzzleEntryTwo = new PuzzleEntry("market", "Farmers ______", Orientation.DOWN, new Point(0, 2));
        PuzzleEntry puzzleEntryThree = new PuzzleEntry("kettle", "It's tea time!", Orientation.ACROSS, new Point(3, 2));
        PuzzleEntry puzzleEntryFour = new PuzzleEntry("extra", "more", Orientation.DOWN, new Point(1, 5));
        entries.add(puzzleEntryOne);
        entries.add(puzzleEntryTwo);
        entries.add(puzzleEntryThree);
        entries.add(puzzleEntryFour);
        Puzzle puzzle = new Puzzle("simple", "simplepuzzle", entries);
        PlayablePuzzle playable = new PlayablePuzzle(puzzle);
        Player player = new Player("p");
        assertTrue(playable.addPlayerEntry(0, player, new PuzzleEntry("stan", "twinkle twinkle", Orientation.ACROSS, new Point(1, 0))));
        assertTrue(playable.addPlayerEntry(1, player, new PuzzleEntry("father", "Farmers ______", Orientation.DOWN, new Point(0, 2))));
        assertTrue(playable.addPlayerEntry(2, player, puzzleEntryThree));
        assertTrue(playable.addPlayerEntry(3, player, puzzleEntryFour));
        assertTrue(playable.addConfirmedEntry(2, puzzleEntryThree));
        assertTrue(playable.addConfirmedEntry(3, puzzleEntryFour));
        assertTrue(playable.deletePlayerEntry(0));
    }
}
