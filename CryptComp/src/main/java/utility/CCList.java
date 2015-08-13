package utility;

import java.util.Iterator;

/**
 * Simple list collection.
 *
 * @param <E> element data type
 */
public class CCList<E> implements Iterable<E> {

    private int size = 0;
    private final int DEFAULT_CAPACITY = 10;
    private Object elements[];

    /**
     * Construct new list collection.
     */
    public CCList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    /**
     * Add entry to collection.
     *
     * @param entry to be added
     */
    public void add(E entry) {
        if (size == elements.length) {
            ensureCapacity();
        }

        elements[size++] = entry;
    }

    /**
     * Retrieve given entry based on its index in collection.
     *
     * @param i entry index
     * @return entry requested
     * @throws IndexOutOfBoundsException if index is out of bounds
     */
    public E get(int i) {
        if (i < 0 || i >= size) {
            throw new IndexOutOfBoundsException("Index: " + i + ", size " + size);
        }

        return ((E) elements[i]);
    }

    /**
     * Remove given element from collection based on its index.
     *
     * @param i index of an element to be removed
     * @return element that was removed
     */
    public E remove(int i) {
        E ele = get(i);

        if (ele != null) {
            compactArray(i);
        }

        return ele;
    }

    /**
     * Number of entries in collection.
     *
     * @return collection current size
     */
    public int getSize() {
        return size;
    }

    /**
     * Check that collection capacity is sufficient for new entry and increase
     * it if necessary.
     */
    private void ensureCapacity() {
        if (size == elements.length) {
            int newSize = elements.length << 1;

            Object[] newValues = new Object[newSize];
            for (int i = 0; i < elements.length; i++) {
                newValues[i] = elements[i];
            }
            elements = newValues;
        }
    }

    /**
     * Compact array from given point forward.
     *
     * @param start index from which compacting starts
     */
    private void compactArray(int start) {
        for (int i = start; i < size; i++) {
            elements[i] = elements[i + 1];
        }
    }

    /**
     * @see java.lang.Iterable
     * @return an Iterator
     */
    @Override
    public Iterator<E> iterator() {
        return new CCListIterator();
    }

    /**
     * Internal class providing iterator interface methods in order to allow
     * iterate over parent class collection.
     */
    private class CCListIterator implements Iterator<E> {

        private int currentPos;

        public CCListIterator() {
            currentPos = 0;
        }

        /**
         * @see java.util.Iterator
         * @return true if the iteration has more elements
         */
        @Override
        public boolean hasNext() {
            return elements[currentPos]!=null;
        }

        /**
         * @see java.util.Iterator
         * @return the next element in the iteration
         */
        @Override
        public E next() {
            E ele = get(currentPos);
            currentPos++;

            return ele;
        }
    }
}
