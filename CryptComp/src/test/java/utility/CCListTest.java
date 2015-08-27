package utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CCList related tests. Added to complete class coverage.
 */
public class CCListTest {

    CCList<Integer> list;

    /**
     * Ensure that size is increased correctly.
     */
    @Test
    public void testSizeIncreas() {
        list = new CCList<>();

        for (int i = 0; i < 11; i++) {
            list.add(i);
        }

        assertEquals(11, list.getSize());
    }

    /**
     * Verify removal of all items.
     */
    @Test
    public void testRemoveAllEntries() {
        list = new CCList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        list.removeAll();

        assertEquals(0, list.getSize());
    }

    /**
     * Check that list is reversed correctly.
     */
    @Test
    public void testReverseList() {
        list = new CCList<>();

        for (int i = 0; i < 10; i++) {
            list.add(i);
        }
        list.reverse();

        for (int i = 0, j = 9; i < 10; i++, j--) {
            assertEquals(i, (int) list.get(j));
        }
    }

    /**
     * Out of bounds, lower, index test
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsLower() {
        list = new CCList<>();
        list.get(-1);
    }

    /**
     * Out of bounds, higher, index test
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsHigher() {
        list = new CCList<>();
        list.get(1);
    }

    /**
     * Add and remove null entry
     */
    @Test
    public void testAddNull() {
        list = new CCList<>();

        list.add((Integer) null);
        assertEquals(1, list.getSize());
        assertNull(list.remove(0));
    }

    /**
     * Out of bounds, high, index test
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsHigh() {
        list = new CCList<>();
        list.add(1);
        list.get(1);
    }

    /**
     * Test helper method converting integer array to CCList.
     */
    @Test
    public void testIntArrayToCCList() {
        int[] testInput = new int[]{65, 66, 66, 65, 32};

        list = CCList.convertArrayToCCListInt(testInput);

        for (int i = 0; i < testInput.length; i++) {
            int value = list.get(i);
            assertEquals(testInput[i], value);
        }
    }

    /**
     * Test helper method converting CCList to integer array.
     */
    @Test
    public void testCCListToIntArray() {
        list = new CCList<>();
        list.add(65);
        list.add(66);
        list.add(66);
        list.add(65);
        list.add(32);
        int[] testInput = CCList.convertCCListIntToArray(list);

        for (int i = 0; i < testInput.length; i++) {
            int value = list.get(i);
            assertEquals(value, testInput[i]);
        }
    }
}
