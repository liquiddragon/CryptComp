package utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CCMap related tests. Added to complete class coverage.
 */
public class CCMapTest {

    /**
     * Add value for same key twice.
     */
    @Test
    public void testAddSameKeyTwice() {
        CCMap<Integer, Integer> map = new CCMap<>();

        map.put(1, 10);
        map.put(1, 0);

        int value = map.get(1);
        assertEquals(0, value);
        assertEquals(1, map.size());
    }

    /**
     * Test null value in map
     */
    @Test
    public void testAddNull() {
        CCMap<Integer, Integer> map = new CCMap<>();

        map.put(1, null);
        map.put(2, 10);

        assertNull(map.get(1));
    }

    /**
     * Remove key-value pair
     */
    @Test
    public void testRemoveKVPair() {
        CCMap<Integer, Integer> map = new CCMap<>();

        map.put(1, 10);
        map.put(2, 20);

        map.remove(1);

        assertEquals(1, map.size());
    }

    /**
     * Try to remove non-existing key-value pair
     */
    @Test
    public void testNonExistingRemoveKVPair() {
        CCMap<Integer, Integer> map = new CCMap<>();

        map.put(1, 10);
        map.put(2, 20);

        map.remove(3);

        assertEquals(2, map.size());
    }
}
