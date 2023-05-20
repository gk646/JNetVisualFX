package gk646.jnet.util.datastructures;

import java.util.AbstractList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

/**
 * A capacity-restricted queue that internally handles shifting if maximal capacity is reached.
 * Provides a backwards iterator when used in for-each.
 *
 * @param <T> the type
 */
public final class LimitedQueue<T> implements Iterable<T> {
    private int size = 0;
    private int maximumCapacity;
    private T[] elements;
    boolean shift;

    @SuppressWarnings("unchecked")
    public LimitedQueue(int maximumCapacity) {
        elements = (T[]) new Object[maximumCapacity];
        this.maximumCapacity = maximumCapacity;
    }
    /**
     * Adds the element to the queue. If the predefined {@link #maximumCapacity} threshold is
     * reached all elements beginning with the second are shifted to the left.
     * The added element is then place at the current at the end.
     * @param obj element whose presence in this collection is to be ensured
     * @return true if the element was added
     */
    public boolean add(T obj) {
        if(shift){
            shift();
        }
        if (size == maximumCapacity) {
            shift  = true;
            elements[maximumCapacity - 1] = obj;
            return true;
        }
        elements[size++] = obj;
        return false;
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
     * Returns a backwards iterator over elements of type {@code T}.
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
     * @param newLimit the new maximum limit
     */
    public void setLimit(int newLimit) {
        this.maximumCapacity = newLimit;
        if (size > newLimit) {
            size = newLimit;
        }
    }
}

