package utility;

import java.util.Iterator;

/**
 * Simple list collection.
 *
 * @param <E> element data type
 */
public class CCList<E> implements Iterable<E> {

    /**
     * Current element count.
     */
    private int size = 0;
    /**
     * Default starting capacity of the list.
     */
    private final int DEFAULT_CAPACITY = 10;
    /**
     * Stored entries array.
     */
    private Object[] elements;

    /**
     * Construct new list collection.
     */
    public CCList() {
        elements = new Object[DEFAULT_CAPACITY];
    }

    /**
     * Construct new list collection based on existing collection.
     *
     * @param copy collection that is copied
     */
    public CCList(CCList<E> copy) {
        int newSize = copy.size;
        newSize = newSize == 0 ? DEFAULT_CAPACITY : newSize;
        elements = new Object[newSize];

        for (int i = 0; i < copy.size; i++) {
            elements[i] = copy.elements[i];
            size++;
        }
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
     * Add collection of entries to this collection.
     *
     * @param entries to be added to this collection
     */
    public void add(CCList<E> entries) {
        for (int i = 0; i < entries.size; i++) {
            this.add(entries.get(i));
        }
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
     * Remove all elements from the collection.
     */
    public void removeAll() {
        elements = new Object[size];
        size = 0;
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
     * Reverse this collection elements.
     */
    public void reverse() {
        Object[] temp = new Object[size];

        for (int i = size - 1; i >= 0; i--) {
            temp[size - i - 1] = elements[i];
        }

        elements = temp;
    }

    /**
     * Check that collection capacity is sufficient for new entry and increase
     * it if necessary.
     */
    private void ensureCapacity() {
        int newSize = elements.length << 1;

        Object[] newValues = new Object[newSize];
        for (int i = 0; i < elements.length; i++) {
            newValues[i] = elements[i];
        }
        elements = newValues;
    }

    /**
     * Compact array from given point forward.
     *
     * @param start index from which compacting starts
     */
    private void compactArray(int start) {
        for (int i = start; i < elements.length - 1; i++) {
            elements[i] = elements[i + 1];
        }
        elements[elements.length - 1] = null;
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
            return elements[currentPos] != null;
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

    /**
     * Convert list collection into integer table
     *
     * @param list collection to be converted
     * @return table containing the collection
     */
    public static int[] convertCCListIntToArray(CCList<Integer> list) {
        int[] returnArray = new int[list.getSize()];

        for (int i = 0; i < list.getSize(); i++) {
            returnArray[i] = list.get(i);
        }

        return returnArray;
    }

    /**
     * Convert integer table into list collection
     *
     * @param list collection to be converted
     * @return table containing the collection
     */
    public static CCList<Integer> convertArrayToCCListInt(int[] list) {
        CCList<Integer> returnList = new CCList<>();

        for (int i = 0; i < list.length; i++) {
            returnList.add(list[i]);
        }

        return returnList;
    }
}
