package crossword;

/**
 * An immutable class representing a guess made by a player
 */
public class Guess {
    private final int id;
    private final String word;
    //Abstraction Function:
    //AF(id, word) --> A Guess object representing a guess a player made. id represents the id of the word on a crossword puzzle
    //                  word represents the current word that is guessed on the board
    //Rep Invariant:
    // true
    //Safety from Rep Expoure:
    // id and word are private and final
    // only getter methods can access id and word
    //Thread Safety argument:
    // This is an immutable so threadsafe
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
