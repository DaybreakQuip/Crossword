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
    
    /**
     * Creates a new player for the game
     * @param id the id of the player
     */
    public Player(String id) {
        this.id = id;
        // The player starts with a score of 0 and has no guesses so far
        this.score = 0;
    }
    
    /**
     * @return the score of the player
     */
    public int getScore() {
        return score;
    }

    /**
     * Adds change to player score
     * @param change change
     */
    public void changeScore(int change) {
        this.score += change;
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
    
}
