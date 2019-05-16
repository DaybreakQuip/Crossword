package crossword;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;

public class PointTest {
    // Partition
    //     Row number = 0, 1, > 1
    //     Column number = 0, 1, > 1 
    
    // Covers: row = 0, column = 0
    @Test
    public void testStartPoint() {
        Point point = new Point(0,0);
        assertEquals(point, new Point(0,0));
    }
    
    // Covers: row > 1, column = 1
    @Test
    public void testOneSimplePoint() {
        Point point = new Point(2,1);
        assertTrue(point.equals(new Point(2,1)));
        assertFalse(point.equals(new Point(78,5)));
    }
    
    // Covers: row = 1, column > 1
    @Test
    public void testAnotherSimplePoint() {
        Point point = new Point(1,16);
        assertTrue(point.equals(new Point(1,16)));
        assertFalse(point.equals(new Point(34,98)));
    }

}
