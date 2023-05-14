package gk646.jnet.util;

import java.util.AbstractList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.RandomAccess;


public final class LimitedQueue<T> extends AbstractList<T> implements RandomAccess, Iterable<T> {
    private int size = 0;
    private T[] elements;

    @SuppressWarnings("unchecked")
    public LimitedQueue(int initialCapacity) {
        elements = (T[]) new Object[initialCapacity];
    }

    @Override
    public boolean add(T obj) {
        if (size == elements.length) {
            size--;
            shift();
            elements[size++] = obj;
            return true;
        }
        elements[size++] = obj;
        return true;
    }

    private void shift() {
        for (int i = 0; i < size-1; i++) {
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
        int num = 0;
        for (T obj : elements) {
            if (obj != null) {
                num++;
            }
        }
        return num;
    }

    public T get(int index) {
        return elements[index];
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

