package utility;

/**
 * Simple key-value class.
 * 
 * @param <K> key data type
 * @param <V> value data type
 */
public class CCEntry<K, V> {

    private final K key;
    private V value;

    /**
     * Create given key-value pair.
     * 
     * @param key key with its data type
     * @param value value with its data type
     */
    public CCEntry(K key, V value) {
        this.key = key;
        this.value = value;
    }

    
    /**
     * Retrieve key.
     * 
     * @return key with used data type
     */
    public K getKey() {
        return key;
    }

    /**
     * Retrieve value.
     * 
     * @return value with used data type
     */
    public V getValue() {
        return value;
    }

    /**
     * Set value.
     * 
     * @param value to be stored
     */
    public void setValue(V value) {
        this.value = value;
    }
}
