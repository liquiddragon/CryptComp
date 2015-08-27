package utility;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * CCTrie class related tests.
 */
public class CCTrieTest {

    CCTrie trie;

    /**
     * Single entry addition.
     */
    @Test
    public void testAdd() {
        trie = new CCTrie();
        int[] entry = new int[]{65};
        trie.add(entry);

        assertEquals(0, trie.get(entry));
    }

    /**
     * Adding three entries overlapping one another.
     */
    @Test
    public void testAddThree() {
        trie = new CCTrie();
        trie.add(new int[]{65});
        trie.add(new int[]{65, 65});
        trie.add(new int[]{65, 65, 66});

        assertEquals(1, trie.get(new int[]{65, 65}));
    }

    /**
     * Reverse lookup test.
     */
    @Test
    public void testGetReverse() {
        trie = new CCTrie();
        trie.add(new int[]{65});
        trie.add(new int[]{65, 66});
        trie.add(new int[]{65, 66, 66});
        trie.add(new int[]{65, 66, 66, 65});

        CCList<Integer> expected = new CCList<>();
        expected.add(65);
        expected.add(66);
        CCList<Integer> result = trie.getReverse(1);

        for (int i = 0; i < result.getSize(); i++) {
            assertEquals(expected.get(i), result.get(i));
        }
    }

    /**
     * Attempt to retrieve non-existing entry.
     */
    @Test
    public void testRetrieveNonExisting() {
        trie = new CCTrie();
        int[] entry = new int[]{65};
        trie.add(entry);

        assertEquals(-1, trie.get(new int[]{66}));
    }

    /**
     * Reverse lookup test for non-existing key.
     */
    @Test
    public void testGetReverseNonExisting() {
        trie = new CCTrie();
        trie.add(new int[]{65});
        trie.add(new int[]{65, 66});
        trie.add(new int[]{65, 66, 66});
        trie.add(new int[]{65, 66, 66, 65});

        CCList<Integer> result = trie.getReverse(10);

        assertNull(result);
    }

    /**
     * Obtain highest used key.
     */
    @Test
    public void testGetHighestKey() {
        trie = new CCTrie();
        int[] entry = new int[]{65};
        trie.add(entry);

        assertEquals(1, trie.highestKey());
    }

    /**
     * Check contains when entry exists.
     */
    @Test
    public void testContainsWithExisting() {
        trie = new CCTrie();
        trie.add(new int[]{65});
        trie.add(new int[]{65, 65});
        trie.add(new int[]{65, 65, 66});

        assertTrue(trie.contains(new int[]{65, 65}));
    }

    /**
     * Check contains when entry does not exist.
     */
    @Test
    public void testContainsWithNonExistingEntry() {
        trie = new CCTrie();
        trie.add(new int[]{65});
        trie.add(new int[]{65, 65});
        trie.add(new int[]{65, 65, 66});

        assertFalse(trie.contains(new int[]{65, 66}));
    }

    /**
     * Check reverse contains when entry exists.
     */
    @Test
    public void testReverseContainsWithExisting() {
        trie = new CCTrie();
        trie.add(new int[]{65});
        trie.add(new int[]{65, 65});
        trie.add(new int[]{65, 65, 66});

        assertTrue(trie.containsReverse(2));
    }

    /**
     * Check reverse contains when entry does not exist.
     */
    @Test
    public void testReverseContainsWithNonExistingEntry() {
        trie = new CCTrie();
        trie.add(new int[]{65});
        trie.add(new int[]{65, 65});
        trie.add(new int[]{65, 65, 66});

        assertFalse(trie.containsReverse(5));
    }
}
