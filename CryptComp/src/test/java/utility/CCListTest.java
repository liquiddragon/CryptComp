package utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CCList related tests. Added to complete class coverage.
 */
public class CCListTest {

    /**
     * Out of bounds, lower, index test
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsLower() {
        CCList<Integer> list = new CCList<>();
        list.get(-1);
    }

    /**
     * Out of bounds, higher, index test
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsHigher() {
        CCList<Integer> list = new CCList<>();
        list.get(1);
    }

    /**
     * Add and remove null entry
     */
    @Test
    public void testAddNull() {
        CCList<Integer> list = new CCList<>();

        list.add((Integer)null);
        assertEquals(1, list.getSize());
        assertNull(list.remove(0));
    }

    /**
     * Out of bounds, high, index test
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void testIndexOutOfBoundsHigh() {
        CCList<Integer> list = new CCList<>();
        list.add(1);
        list.get(1);
    }
}
