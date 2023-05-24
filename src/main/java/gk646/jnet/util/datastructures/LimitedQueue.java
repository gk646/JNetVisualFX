package gk646.jnet.util.datastructures;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A capacity-restricted queue that internally handles shifting if maximal capacity is reached.
 * Provides a backwards iterator when used in for-each.
 *
 * @param <T> the type
 */
public final class LimitedQueue<T> implements Iterable<T> {
    private final int max;
    boolean shift;
    private int size = 0;
    private int maximumCapacity;
    private T[] elements;

    @SuppressWarnings("unchecked")
    public LimitedQueue(int maximumCapacity) {
        elements = (T[]) new Object[maximumCapacity];
        this.maximumCapacity = maximumCapacity;
        this.max = maximumCapacity;
    }

    /**
     * Adds the element to the queue. If the predefined {@link #maximumCapacity} threshold is
     * reached all elements beginning with the second are shifted to the left.
     * The added element is then place at the current at the end.
     *
     * @param obj element whose presence in this collection is to be ensured
     */
    public void add(T obj) {
        if (shift) {
            shift();
        }
        if (size == maximumCapacity) {
            shift = true;
            elements[maximumCapacity - 1] = obj;
            return;
        }
        elements[size++] = obj;
        shift = false;
    }

    private void shift() {
        for (int i = 0; i < maximumCapacity - 1; i++) {
            elements[i] = elements[i + 1];
        }
    }

    public int size() {
        return size;
    }

    /**
     * Returns elements counting from the back.
     * If the index is out of bounds returns null.
     *
     * @param elementIndex index of the element to return
     * @return the element at index currentSize-elementIndex or null
     */
    public T get(int elementIndex) {
        if (size - elementIndex - 1 <= 0) return elements[0];
        return elements[size - elementIndex - 1];
    }

    /**
     * The usual direct access for a list
     *
     * @param index the access index
     * @return the element at index
     */
    public T directGet(int index) {
        return elements[index];
    }

    /**
     * Sets the element at index i to s
     *
     * @param s the given string value
     * @param i the index to set
     */
    public void set(T s, int i) {
        elements[i] = s;
    }

    /**
     * Returns a backwards iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int currentIndex = size - 1;

            @Override
            public boolean hasNext() {
                return currentIndex > -1;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[currentIndex--];
            }
        };
    }


    @SuppressWarnings("unchecked")
    public void clear() {
        elements = (T[]) new Object[maximumCapacity];
        size = 0;
    }


    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        for (int i = 0; i < maximumCapacity; i++) {
            string.append(directGet(i)).append("\n");
        }
        return string.toString();
    }

    /**
     * Sets a new maximum limit. Currently, this method won't extend the list past its creation size, so it only works downwards.
     *
     * @param newLimit the new maximum limit
     */
    public void setLimit(int newLimit) {
        if (newLimit > max || newLimit == 0) {
            return;
        }
        this.maximumCapacity = newLimit;
        if (size > newLimit) {
            size = newLimit;
        }
    }
}

