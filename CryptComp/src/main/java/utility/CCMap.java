package utility;

/**
 * Simple map collection.
 *
 * @param <K> key data type
 * @param <V> value data type
 */
public class CCMap<K, V> {

    private int size = 0;
    private final int DEFAULT_CAPACITY = 16;

    private CCEntry<K, V>[] values = new CCEntry[DEFAULT_CAPACITY];

    /**
     * Retrieve value for given key
     *
     * @param key for which value is wanted for
     * @return value, if found and null otherwise
     */
    public V get(K key) {
        for (int i = 0; i < size; i++) {
            if (values[i] != null) {
                if (values[i].getKey().equals(key)) {
                    return values[i].getValue();
                }
            }
        }
        return null;
    }

    /**
     * Add given key-value pair to collection.
     *
     * @param key to be added
     * @param value to be added
     */
    public void put(K key, V value) {
        boolean insert = true;

        for (int i = 0; i < size; i++) {
            if (values[i].getKey().equals(key)) {
                values[i].setValue(value);
                insert = false;
            }
        }

        if (insert) {
            ensureCapacity();
            values[size++] = new CCEntry<>(key, value);
        }
    }

    /**
     * Number of entries in collection.
     *
     * @return collection actual size
     */
    public int size() {
        return size;
    }

    /**
     * Remove given key-value pair from collection.
     *
     * @param key for which value pair should be removed
     */
    public void remove(K key) {
        for (int i = 0; i < size; i++) {
            if (values[i].getKey().equals(key)) {
                values[i] = null;
                size--;
                compactArray(i);
            }
        }
    }

    /**
     * Check whether given key exists in collection.
     *
     * @param key for which existence is wanted to know
     * @return true if key exists in collection, false otherwise
     */
    public boolean containsKey(K key) {
        return get(key) != null;
    }

    /**
     * Compact array from given point forward.
     *
     * @param start index from which compacting starts
     */
    private void compactArray(int start) {
        for (int i = start; i < size; i++) {
            values[i] = values[i + 1];
        }
    }

    /**
     * Check that collection capacity is sufficient for new entry and increase
     * it if necessary.
     */
    private void ensureCapacity() {
        if (size == values.length) {
            int newSize = values.length << 1;
            CCEntry<K, V>[] newValues = new CCEntry[newSize];

            for (int i = 0; i < values.length; i++) {
                newValues[i] = values[i];
            }

            values = newValues;
        }
    }
}
