package deque;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class LinkedListDeque<T> implements Deque<T> ,Iterable<T> {

    private static class Node<T> {
        T value;
        Node<T> prev;
        Node<T> next;

        Node(T v) { value = v; }
    }

    private final Node<T> sentinel;   // 只保存头尾引用
    private int size;

    public LinkedListDeque() {
        sentinel = new Node<>(null);
        sentinel.prev = null;   // head
        sentinel.next = null;   // tail
        size = 0;
    }

    @Override
    public void addFirst(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.next = sentinel.prev;   // 原 head
        newNode.prev = null;

        if (sentinel.prev != null) {          // 非空
            sentinel.prev.prev = newNode;
        } else {                              // 空表，头=尾
            sentinel.next = newNode;
        }
        sentinel.prev = newNode;              // 更新 head
        size++;
    }

    @Override
    public void addLast(T item) {
        Node<T> newNode = new Node<>(item);
        newNode.prev = sentinel.next;   // 原 tail
        newNode.next = null;

        if (sentinel.next != null) {          // 非空
            sentinel.next.next = newNode;
        } else {                              // 空表，头=尾
            sentinel.prev = newNode;
        }
        sentinel.next = newNode;              // 更新 tail
        size++;
    }

    @Override
    public int     size()     { return size; }

    @Override
    public void printDeque() {
        for (Node<T> p = sentinel.prev; p != null; p = p.next) {
            System.out.print(p.value + " ");
        }
        System.out.println();
    }

    @Override
    public T removeFirst() {
        if (isEmpty()) return null;
        Node<T> head = sentinel.prev;
        T val = head.value;

        Node<T> newHead = head.next;
        if (newHead != null) {
            newHead.prev = null;
        } else {                     // 删除后变空
            sentinel.next = null;
        }
        sentinel.prev = newHead;
        size--;
        return val;
    }

    @Override
    public T removeLast() {
        if (isEmpty()) return null;
        Node<T> tail = sentinel.next;
        T val = tail.value;

        Node<T> newTail = tail.prev;
        if (newTail != null) {
            newTail.next = null;
        } else {                     // 删除后变空
            sentinel.prev = null;
        }
        sentinel.next = newTail;
        size--;
        return val;
    }

    @Override
    public T get(int index) {
        if (index < 0 || index >= size) return null;
        Node<T> p = sentinel.prev;           // head
        for (int i = 0; i < index; i++) {
            p = p.next;
        }
        return p.value;
    }

    public T getRecursive(int index) {
        if (index < 0 || index >= size) return null;
        return getRecHelper(sentinel.prev, index);
    }

    private T getRecHelper(Node<T> node, int idx) {
        return (idx == 0) ? node.value : getRecHelper(node.next, idx - 1);
    }


    @Override
    public Iterator<T> iterator() {
        return new Iterator<>() {
            private Node<T> cur = sentinel.prev;      // 从头开始
            @Override public boolean hasNext() { return cur != null; }
            @Override public T next() {
                if (!hasNext()) throw new NoSuchElementException();
                T val = cur.value;
                cur = cur.next;
                return val;
            }
        };
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LinkedListDeque<?> other)) return false;
        if (this.size != other.size) return false;

        Iterator<T> it1 = this.iterator();
        Iterator<?> it2 = other.iterator();
        while (it1.hasNext() && it2.hasNext()) {
            if (!it1.next().equals(it2.next())) return false;
        }
        return true;
    }
}
