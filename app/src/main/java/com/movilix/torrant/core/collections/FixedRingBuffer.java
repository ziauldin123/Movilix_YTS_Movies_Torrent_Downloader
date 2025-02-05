

package com.movilix.torrant.core.collections;

import java.util.Arrays;
import java.util.Iterator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/*
 * A ring buffer (circular buffer) with max and init capacity.
 */

public class FixedRingBuffer<E> implements Iterable<E>
{
    private E[] elements;
    private int head = -1;
    private int tail;
    private int capacity;

    public FixedRingBuffer(int capacity)
    {
        this(0, capacity);
    }

    public FixedRingBuffer(int initCapacity, int capacity)
    {
        if (initCapacity < 0)
            throw new IllegalArgumentException("Initial capacity must be greater or equal 0");

        if (capacity <= 0)
            throw new IllegalArgumentException("Capacity must be greater than 0");

        if (initCapacity > capacity)
            throw new IllegalArgumentException("Initial capacity cannot be greater than capacity");

        this.capacity = capacity;
        elements = (E[])new Object[initCapacity == 0 ? capacity : initCapacity];
    }

    public int size()
    {
        if (head == -1)
            return 0;
        else if (tail <= head)
            return (capacity - head) + tail;
        else
            return tail - head;
    }

    public int getAllocatedSize()
    {
        return elements.length;
    }

    public void add(@NonNull Iterable<E> list)
    {
        for (E e : list)
            add(e);
    }

    public void add(E element)
    {
        tryGrow();

        if (head < 0)
            head = 0;
        else if (tail == head)
            head = ++head % capacity;

        elements[tail] = element;
        tail = ++tail % capacity;
    }

    @Nullable
    public E remove(E element)
    {
        if (element == null)
            return null;

        int index = indexOf(element);
        if (index != -1)
            return removeAt(index);

        return null;
    }

    public int indexOf(E element)
    {
        if (element == null)
            return -1;

        for (int i = 0; i < size(); i++) {
            if (element.equals(elements[(head + i) % capacity]))
                return i;
        }

        return -1;
    }

    public E removeAt(int index)
    {
        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);

        E res = elements[(head + index) % capacity];
        for (int i = index; i < size - 1; i++)
            elements[(head + i) % capacity] = elements[(head + i + 1) % capacity];

        elements[(head + size - 1) % capacity] = null;

        if (tail == 0)
            tail = size - 1;
        else
            tail--;

        if (tail == head) {
            if (head >= 0)
                head--;
        }

        return res;
    }

    public E get(int index)
    {
        int size = size();
        if (index < 0 || index >= size)
            throw new IndexOutOfBoundsException("index = " + index + ", size = " + size);

        return elements[(head + index) % capacity];
    }

    public void clear()
    {
        if (isEmpty())
            return;

        Arrays.fill(elements, 0, size() - 1, null);

        resetHead();
    }

    public boolean isEmpty()
    {
        return size() == 0;
    }

    public boolean isFull()
    {
        return size() == capacity;
    }

    public boolean contains(E element)
    {
        for (int i = 0; i < size(); i++) {
            E e = elements[(head + i) % capacity];
            if ((element == null && e == null) || (element != null && element.equals(e)))
                return true;
        }

        return false;
    }

    private void resetHead()
    {
        head = -1;
        tail = 0;
    }

    private void tryGrow()
    {
        if (size() < elements.length || elements.length == capacity)
            return;

        int newSize = Math.min(elements.length * 2, capacity);
        E[] newArray = (E[])new Object[newSize];
        System.arraycopy(elements, 0, newArray, 0, elements.length);
        elements = newArray;
    }

    @NonNull
    @Override
    public Iterator<E> iterator()
    {
        return new FixedRingBufferIterator();
    }

    private class FixedRingBufferIterator implements Iterator<E>
    {
        private int index;

        @Override
        public boolean hasNext()
        {
            return index < size();
        }

        @Override
        public E next()
        {
            return get(index++);
        }
    }
}
