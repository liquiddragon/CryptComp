package utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CCMap related tests. Added to complete class coverage.
 */
public class CCMapTest {

    CCMap<Integer, Integer> map;

    /**
     * Ensure that size is increased properly.
     */
    @Test
    public void testSizeIncrease() {
        map = new CCMap<>();

        for (int i = 0; i < 17; i++) {
            map.put(i, 16 - i);
        }

        assertEquals(17, map.size());
    }

    /**
     * Add value for same key twice.
     */
    @Test
    public void testAddSameKeyTwice() {
        map = new CCMap<>();

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
        map = new CCMap<>();

        map.put(1, null);
        map.put(2, 10);

        assertNull(map.get(1));
    }

    /**
     * Remove key-value pair
     */
    @Test
    public void testRemoveKVPair() {
        map = new CCMap<>();

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
        map = new CCMap<>();

        map.put(1, 10);
        map.put(2, 20);

        map.remove(3);

        assertEquals(2, map.size());
    }

    /**
     * Remove key-value pair twice.
     */
    @Test
    public void testRemoveKVPairTwice() {
        map = new CCMap<>();

        map.put(1, 10);
        map.put(2, 20);

        map.remove(2);
        map.remove(2);

        assertEquals(1, map.size());
    }

}
