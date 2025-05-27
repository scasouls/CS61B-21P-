package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class ArrayDeque<T> implements  Deque<T> ,Iterable<T> {

    private static final int DEFAULT_CAPACITY = 8;

    private T[] array;
    private int front;
    private int rear;
    private int size;
    private int capacity;

    @SuppressWarnings("unchecked")
    public ArrayDeque() {
        capacity = DEFAULT_CAPACITY;
        array    = (T[]) new Object[capacity];
        front = rear = size = 0;
    }

    public ArrayDeque(int cap){
        capacity = cap;
        array = (T[]) new Object[capacity];
        front = rear = size = 0;
    }

    @Override
    public int     size()        { return size;     }

    @SuppressWarnings("unchecked")
    private void resize(int newCap) {
        T[] newArr = (T[]) new Object[newCap];
        for (int i = 0; i < size; i++) {
            newArr[i] = array[(front + i) % capacity];
        }
        array    = newArr;
        capacity = newCap;
        front    = 0;
        rear     = size;
    }

    private void maybeShrink() {
        if (capacity > DEFAULT_CAPACITY && size <= capacity / 4) {
            resize(capacity / 2);
        }
    }

    @Override
    public void addFirst(T item) {
        if (size == capacity) resize(capacity * 2);
        front = (front - 1 + capacity) % capacity; // 先递减索引再写入
        array[front] = item;
        size++;
    }

    @Override
    public void addLast(T item) {
        if (size == capacity) resize(capacity * 2);
        array[rear] = item;
        rear = (rear + 1) % capacity;
        size++;
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) return null;
        T val = array[front];
        array[front] = null;
        front = (front + 1) % capacity;
        size--;
        maybeShrink();
        return val;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) return null;
        rear = (rear - 1 + capacity) % capacity;
        T val = array[rear];
        array[rear] = null;
        size--;
        maybeShrink();
        return val;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        return array[(front + index) % capacity];
    }

    @Override
    public void printDeque() {
        for (int i = 0; i < size; i++) {
            System.out.print(array[(front + i) % capacity] + " ");
        }
        System.out.println();
    }

    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private int idx = 0;
            @Override public boolean hasNext()             { return idx < size; }
            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                return array[(front + idx++) % capacity];
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;                      // 同一个对象
        if (!(o instanceof ArrayDeque<?> other)) return false; // 类型检查

        if (this.size != other.size) return false;       // 长度不同

        for (int i = 0; i < size; i++) {
            T thisItem = this.get(i);
            Object otherItem = other.get(i);
            if (thisItem == null) {
                if (otherItem != null) return false;
            } else {
                if (!thisItem.equals(otherItem)) return false;
            }
        }

        return true;
    }
}
