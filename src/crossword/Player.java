package crossword;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Mutable, threadsafe class that represents a player in the crossword game
 *
 */
public class Player {
    public static final String EMPTY_PLAYER_ID = "EMPTY_PLAYER";
    
    private final String id;
    private int score;
    private List<Guess> guesses;
    
    /**
     * Creates a new player for the game
     * @param id the id of the player
     */
    public Player(String id) {
        this.id = id;
        // The player starts with a score of 0 and has no guesses so far
        this.score = 0;
        this.guesses = Collections.synchronizedList(new ArrayList<>());
    }
    
    /**
     * Adds a guess for the player
     * @param guess the guess to add
     */
    public void addGuess(Guess guess) {
        this.guesses.add(guess);
    }
    
    /**
     * Removes a guess from the player
     * @param guess a guess to remove from the player
     */
    public void removeGuess(Guess guess) {
        this.guesses.remove(guess);
    }
    
    /**
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * @return the id of the player
     */
    public String getId() {
        return id;
    }
    
    /**
     * @return true if the player is empty (not a real player) and false otherwise
     */
    public boolean isEmpty() {
        return id.equals(EMPTY_PLAYER_ID);
    }
    
    /**
     * @return a list of guesses that the player has made
     */
    public List<Guess> getGuesses() {
        return new ArrayList<>(guesses);
    }
}
