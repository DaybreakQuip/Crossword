package crossword;

import java.util.Map;

/**
 * TODO: Implement this class
 * Mutable class representing a crossword game match between two players
 */
public class Match {
    // Abstraction Function:
    // AF(matchId, description, puzzle, playerOne, playerTwo, state) = A match that has a match ID (matchID), a description,
    //                                                      a playable puzzle, and two players that are playing in the match 
    //                                                      (playerOne and playerTwo). The state of the match can be waiting 
    //                                                      (for a second player), ongoing (when the match has two players), and
    //                                                      done (if all words in the puzzle are confirmed).
    // Rep Invariant:
    //      true
    // Safety From Rep Exposure:
    //  all fields are private
    //  matchId and puzzle are final so they are not reassignable
    //  matchId and state are immutable
    //  puzzle is never returned directly to clients so it cannot be modified by clients
    //  clients only pass in player ids and never get access to the player objects so they cannot modify the players
    // Thread safety argument:
    //  TODO: Monitor Pattern
    private final String matchId;
    private final String description;
    private final PlayablePuzzle puzzle;
    private Player playerOne;
    private Player playerTwo;
    private MatchState state;
    
    /**
     * Creates a match with a match_id and a template puzzle
     *  A match requires at least one player to be created
     * @param matchId the id of the match
     * @param description the description of the match
     * @param templatePuzzle the template puzzle to create the playable puzzle from
     * @param playerOneId the first player's id for the game
     */
    public Match(String matchId, String description, Puzzle templatePuzzle, String playerOneId) {
        this.state = MatchState.WAITING;
        this.description = description;
        this.matchId = matchId;
        playerOne = new Player(playerOneId);
        playerTwo = new Player(Player.EMPTY_PLAYER_ID);
        puzzle = new PlayablePuzzle(templatePuzzle);
    }
    
    /**
     * @return id of the match
     */
    public String getMatchId() {
        return this.matchId;
    }
    
    /**
     * @return description of the match
     */
    public String getDescription() {
        return this.description;
    }
    
    /**
     * Add a player to the match if the match is not full yet
     * @param playerId the id of the player to add, the new player id cannot be the same
     *          as the player that is already in match
     * @return true of the player was added to the match and false otherwise
     */
    public boolean joinMatch(String playerId) {
        if (isWaiting()) {
            playerTwo = new Player(playerId);
            return true;
        }
        return false;
    }
    
    /**
     * @return true if the match is ongoing and false otherwise
     */
    public boolean isOngoing() {
        return state == MatchState.ONGOING;
    }
    
    /**
     * @return true if the match is waiting and false otherwise
     */
    public boolean isWaiting() {
        return state == MatchState.WAITING;
    }
    
    /**
     * @return true if the match is done and false otherwise
     */
    public boolean isDone() {
        return state == MatchState.DONE;
    }
    
    /**
     * Returns the player with the corresponding id in the match
     * @param playerId the id of the player, the player id must exist in the match
     * @return the Player with the corresponding player id in the match
     */
    private Player getPlayer(String id) {
        throw new RuntimeException("Not Implemented!");
    }
    
    /**
     * Tries to make a guess for the match puzzle for the given player id
     * The rules for a try guess are as follows:
     *   1) the id corresponds to either an empty word or
     *   2) an un-confirmed word entered by the same user and false otherwise
     * @param playerId the id of the player making a guess
     * @param guess the guess of the player
     * @return true if the guess is valid and false otherwise
     */
    public boolean tryWord(String playerId, Guess guess) {
        throw new RuntimeException("Not Implemented!");
    }
    
    /**
     * Tries to challenge a guess for the match puzzle for the given player id
     * The rules for a challenge guess are as follows:
     *      A challenge is valid if:
     *          a) the challenged word was entered by the other player (can’t challenge your own words)
     *          b) challenged word was not already confirmed
     *          c) your proposed word is different from the word already there
     *          d) your proposed word must be of the correct length
     *      If a challenge is valid:
     *          1) if the original word is correct,  then it gets confirmed as correct and can no longer 
     *              be challenged or changed. The challenger loses one point for an incorrect challenge.
     *          2) if the original word is incorrect: 
     *              2A) if the challenger is correct, then the challenger 
     *                  gets the word and it gets confirmed. The challenger also gets two bonus points for a 
     *                  correct challenge. An important property of a correct challenge is that a challenge does 
     *                  not have to be consistent with any incorrect words already in the puzzle. Any word that 
     *                  is inconsistent with the correct challenge will be cleared without penalty regardless of 
     *                  who had entered it.
     *              2B) if the challenger is incorrect, then  the word is cleared from the board, and the challenger 
     *                  loses one point for an incorrect challenge.
     *      Else if the challenge is invalid: 
     *          The challenge is invalid
     * @param playerId the id of the player making a guess
     * @param guess the guess of the player
     * @return true if the challenge is valid and false otherwise
     */
    public boolean challengeWord(String playerId, Guess guess) {
        throw new RuntimeException("Not Implemented!");
    }

    /**
     * Forfeits a player from the match and ends the match
     * @param playerId the player id of the player that is forfeiting
     */
    public void forfeit(String playerId) {
        throw new RuntimeException("Not Implemented!");
    }
    
    /**
     * Returns a string of the playable puzzle with a specific format where every entry 
     *  is separated by a delimiter and words are populated based on the playable puzzle's 
     *  confirmed and guessed entries
     * @return a representation of the puzzle with entries separated by delimeters of the same kind
     */
    //TODO TONIGHT
    public String getPuzzleForResponse() {
        throw new RuntimeException("Not Implemented!");
    }
    
    /**
     * @return string representing the score of each player
     */
    public String showScore() {
        throw new RuntimeException("Not Implemented!");
    }
}
