package gk646.jnet.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;

/**
 * A capacity-restricted queue that internally handles shifting if maximal capacity is reached.
 * Provides a backwards iterator when used in for-each.
 *
 * @param <T> the type
 */
public final class LimitedQueue<T> extends AbstractList<T> implements RandomAccess, Iterable<T> {
    private int size = 0;
    private final int maximumCapacity;
    private T[] elements;

    @SuppressWarnings("unchecked")
    public LimitedQueue(int maximumCapacity) {
        elements = (T[]) new Object[maximumCapacity];
        this.maximumCapacity = maximumCapacity;
    }

    /**
     * Adds the element to the queue. If the predefined {@link #maximumCapacity} threshold is
     * reached all elements beginning with the first are shifted to the right.
     * The last element will then be overwritten by the added element.
     *
     * @param obj element whose presence in this collection is to be ensured
     * @return true if the element was added
     */
    @Override
    public boolean add(T obj) {
        System.out.println(size);
        if (size == maximumCapacity - 1) {
            shift();
            elements[size] = obj;
            return true;
        }
        elements[size++] = obj;
        return true;
    }

    private void shift() {
        for (int i = 0; i < size; i++) {
            elements[i] = elements[i + 1];
        }
    }

    @Override
    public T remove(int index) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T removedElement = elements[index];
        System.arraycopy(elements, index + 1, elements, index, size - index - 1);
        elements[--size] = null;
        return removedElement;
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
        if (size - elementIndex - 1 < 0) return elements[0];
        return elements[size - elementIndex - 1];
    }

    /**
     * Returns a backwards iterator over elements of type {@code T}.
     *
     * @return an Iterator.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private int currentIndex = size;

            @Override
            public boolean hasNext() {
                return currentIndex >= 0;
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException();
                }
                return elements[currentIndex--];
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException("remove");
            }
        };
    }


    @Override
    @SuppressWarnings("unchecked")
    public void clear() {
        elements = (T[]) new Object[elements.length];
        size = 0;
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        int span = toIndex - fromIndex;
        LimitedQueue<T> result = new LimitedQueue<>(span);
        result.size = span;
        System.arraycopy(this.elements, fromIndex, result.elements, 0, span);
        return result;
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }

        if (!(obj instanceof LimitedQueue<?>)) {
            return false;
        }

        LimitedQueue<T> limitedQueue = (LimitedQueue<T>) obj;

        if (limitedQueue.size == size) {
            for (int i = 0; i < size; i++) {
                if (!limitedQueue.get(i).equals(elements[i])) {
                    return false;
                }
            }
            return true;
        }

        return false;
    }

    @Override
    public T set(int index, T element) {
        if (index < 0 || index >= size) {
            throw new IndexOutOfBoundsException("Index: " + index + ", Size: " + size);
        }
        T oldValue = elements[index];
        elements[index] = element;
        return oldValue;
    }

    @Override
    public int hashCode() {
        int result = 1;
        for (int i = 0; i < size; i++) {
            result = 31 * result + (elements[i] == null ? 0 : elements[i].hashCode());
        }
        return result;
    }
}

