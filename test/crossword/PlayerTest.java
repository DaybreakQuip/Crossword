package crossword;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

public class PlayerTest {
    // Partitions:
    //     Length of player id = 1, > 1
    //     Player score = 0, 1, > 1

    // Covers: length of player id = 1, player score = 0
    @Test
    public void testPlayer() {
        Player player = new Player("p");
        assertEquals(1, player.getId().length());
        assertEquals(0, player.getScore());
    }
    
    // Covers: length of player id > 1, player score = 1
    @Test
    public void testScoreChange() {
        Player player = new Player("6.031");
        assertTrue(player.getId().length() > 1);
        player.changeScore(1);
        assertEquals(1, player.getScore());
    }
    
    // Covers: length of player id > 1, player score > 1
    @Test
    public void testScoreBigChange() {
        Player player = new Player("6.031");
        assertTrue(player.getId().length() > 1);
        player.changeScore(49);
        assertEquals(49, player.getScore());
    }
}
