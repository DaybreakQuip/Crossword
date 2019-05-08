package crossword;

/**
 * An immutable class representing a guess made by a player
 */
public class Guess {
    private final int id;
    private final String word;
    
    /**
     * Creates a new guess
     * @param id puzzle entry id of the word
     * @param word of the guess
     */
    public Guess(int id, String word) {
        this.id = id;
        this.word = word;
    }
    
    /**
     * @return Puzzle entry id for this guess
     */
    public int getId() {
        return id;
    }
    
    /**
     * @return word for this guess
     */
    public String getWord() {
        return word;
    }
}
