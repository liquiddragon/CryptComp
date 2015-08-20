package utility;

/**
 * Simple trie with reverse travels included.
 */
public class CCTrie {

    private final int nodeSpan = 256;
    private final int nan = -1;

    /**
     * Internal node class for the trie.
     */
    private class Node {

        private int value;
        private final Node[] next = new Node[nodeSpan];
        private Node previous;
        private int previousValue;

        /**
         * Default constructor.
         */
        public Node() {
            value = nan;
        }
    }

    private Node root;
    private int elementCount;
    private int lastValue = 0;
    private final CCMap<Integer, Node> reverseTable;

    /**
     * Construct new trie collection.
     */
    public CCTrie() {
        elementCount = 0;
        reverseTable = new CCMap<>();
    }

    /**
     * Add new entry table into the trie.
     *
     * @param entry to be added
     */
    public void add(int[] entry) {
        root = add(root, entry, 0);
    }

    /**
     * Recursive routine to add node to trie and reverse map collections.
     *
     * @param x node being handled
     * @param entry to be added to the collection
     * @param depth where travels in trie executes presently
     * @return node added or one that was travelled to
     */
    private Node add(Node x, int[] entry, int depth) {
        if (x == null) {
            x = new Node();
        }
        
        if (depth == entry.length) {
            if (x.value < 0) {
                elementCount++;
            }
            
            x.value = lastValue;
            reverseTable.put(lastValue, x);
            lastValue++;
            return x;
        }
        
        int c = entry[depth];
        x.next[c] = add(x.next[c], entry, depth + 1);

        if (x.next[c] != null && x.next[c].previous == null) {
            x.next[c].previous = x;
            x.next[c].previousValue = c;
        }

        return x;
    }

    /**
     * Obtain value corresponding to given entry.
     *
     * @param entry which value is requested
     * @return requested value or NAN, -1, if unavailable
     */
    public int get(int[] entry) {
        int count;

        Node x = get(root, entry, 0);

        if (x == null) {
            count = nan;
        } else {
            count = x.value;
        }

        return count;
    }

    /**
     * Get entry from the collection based on its value.
     *
     * @param value that corresponds entry requested
     * @return requested entry or null if not found
     */
    public CCList<Integer> getReverse(int value) {
        CCList<Integer> result = null;

        if (reverseTable.containsKey(value)) {
            Node last = reverseTable.get(value);
            result = new CCList<>();
        
            while (last != root) {
                result.add(last.previousValue);
                last = last.previous;
            }
        }

        // Reverse obtained list
        if (result != null && result.getSize() > 1) {
            result.reverse();
        }
        
        return result;
    }

    /**
     * Retrieve requested entry from the collection.
     *
     * @param x node where search is started
     * @param entry being searched for
     * @param depth at which traverse goes
     * @return requested entry or null if not found
     */
    private Node get(Node x, int[] entry, int depth) {
        if (x == null) {
            return null;
        }
        
        if (depth == entry.length) {
            return x;
        }
        
        return get(x.next[entry[depth]], entry, depth + 1);
    }

    /**
     * Check if requested entry exists in the collection.
     *
     * @param entry being verified
     * @return true if found, false otherwise
     */
    public boolean contains(int[] entry) {
        boolean result = true;

        Node x = get(root, entry, 0);
        
        if (x == null) {
            result = false;
        }

        return result;
    }

    /**
     * Check if given value corresponding entry exists in the collection.
     *
     * @param key thats existence in the collection is checked
     * @return true if found, otherwise false
     */
    public boolean containsReverse(int key) {
        boolean result = false;

        if (reverseTable.containsKey(key)) {
            result = true;
        }

        return result;
    }

    /**
     * Provides highest key value in the collection.
     *
     * @return highest key
     */
    public int highestKey() {
        return lastValue;
    }
}
