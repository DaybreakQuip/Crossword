package crossword;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import edu.mit.eecs.parserlib.UnableToParseException;

/**
 * TODO: Make this class mutable for future milestones (?)
 * An immutable Game class representing a single Crossword Game
 *
 */
public class Game {
    private final Map<String, Puzzle> puzzles; // map of name : puzzle
    private static final String ENTRY_DELIM = "~";
    private static final String WORD_DELIM = "`";
    /**
     * Main method. Makes the Game from Puzzle objects from parsing.
     * 
     * @param args command line arguments, not used
     * @throws UnableToParseException if example expression can't be parsed
     * @throws IOException if .puzzle file cannot be opened
     */
    public static void main(final String[] args) throws UnableToParseException, IOException {
        System.out.println(Game.parseGameFromFiles("puzzles/").getPuzzleNames());
    }
    /**
     * @param directory the folder to the puzzles
     * @return a new Game with all puzzles parsed from files inside puzzles/
     * @throws UnableToParseException Puzzle cannot be parsed
     * @throws IOException File cannot be read
     */
    public static Game parseGameFromFiles(String directory) throws UnableToParseException, IOException {
        // Do something
        HashMap<String, Puzzle> puzzles = new HashMap<>();
        File folder = new File(directory);
        File[] listofPuzzles = folder.listFiles();
        for (File file : listofPuzzles) {
            if (file.isFile()) {
                Puzzle newPuzzle = PuzzleParser.parse(puzzleFromFile(file.getName()));
                if (newPuzzle.isConsistent()) {
                    puzzles.put(newPuzzle.getName(), newPuzzle);
                }
            }
        }
        return new Game(puzzles);
    }
    /**
     * 
     * @param filename the filename of the puzzle String to be extracted from
     * @return String of the files to be parsed
     * @throws IOException 
     */
    private static String puzzleFromFile(String filename) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("puzzles/" + filename));
        StringBuilder inputBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            inputBuilder.append(line + "\n");
        }
        reader.close();
        return inputBuilder.toString();
    }
    /**
     * Returns a game meant for testing purposes
     * @return a new dummy game with puzzle entries from simple.puzzle
     */
    public static Game createDummyGame() {
        Puzzle dummyPuzzle = Puzzle.makeDummyPuzzle();
        // then put the puzzle in a map
        Map<String, Puzzle> puzzles = new HashMap<>();
        puzzles.put(dummyPuzzle.getName(), dummyPuzzle);
        // lastly, initialize and a return a game with that map 
        return new Game(puzzles);
    }
    
    /**
     * Creates a new Game
     * @param puzzles map of name : puzzles that are valid (consistent)
     */
    public Game(Map<String, Puzzle> puzzles) {
        this.puzzles = Collections.unmodifiableMap(new HashMap<>(puzzles));
    }
    
    /**
     * Returns a puzzle with specified name
     * @param name the name of the puzzle
     * @return puzzle from the game if the name exists
     */
    public Puzzle getPuzzle(String name) {
        return this.puzzles.get(name);
    }
    
    /**
     * Returns a puzzle with a specific format where every entry is separate by new lines and no 
     * words are revealed
     * @param name the name of the puzzle
     * @return string with format: length, clue, orientation, row, col\n
     *         e.g: "4, "twinkle twinkle", ACROSS, 0, 1\n"
     */
    public String getPuzzleForResponse(String name) {
        Puzzle puzzle = puzzles.get(name);
        String puzzleString = "";
        for (PuzzleEntry entry: puzzle.getEntries()) {
            puzzleString += entry.getWord().length() + WORD_DELIM + entry.getClue() + WORD_DELIM + 
                            entry.getOrientation() + WORD_DELIM + entry.getPosition().getRow() 
                            + WORD_DELIM + entry.getPosition().getCol() + ENTRY_DELIM;
        }
        return puzzleString.substring(0,puzzleString.length()-1);
    }
    
    /**
     * @return set of the names of all puzzles in the game
     */
    public Set<String> getPuzzleNames() {
        return puzzles.keySet();
    }
    
    @Override
    public int hashCode() {
        return puzzles.hashCode();
    }
    
    @Override
    public boolean equals(Object other) {
        return other instanceof Game && sameValue((Game) other);
    }

    /**
     * @param other the other game to compare to
     * @return true if this and other game are equal and false otherwise
     */
    public boolean sameValue(Game other) {
        return puzzles.equals(other.puzzles);
    }
    
}
