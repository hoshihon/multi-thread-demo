package com.github.hoshihon;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class ConcurrentLinkedQueue<T> {

    volatile Node<T> head;
    volatile Node<T> tail;

    ReadWriteLock queueLock = new ReentrantReadWriteLock(true);

    ReentrantLock headLock = new ReentrantLock();
    ReentrantLock tailLock = new ReentrantLock();
    AtomicInteger size = new AtomicInteger(0);

    public static class Node<E> {
        E element;

        Node<E> prev;
        Node<E> next;

        public Node(E element) {
            this.element = element;
        }
    }

    int size() {
        return size.get();
    }

    public void push(T obj) {
        Node<T> newNode = new Node<>(obj);

        while (true) {
            Node<T> tmpTail = tail;
            int tmpSize = size.get();
            if (tmpTail != null) {
                queueLock.readLock().lock();
                tailLock.lock();
                try {
                    if (tmpTail != tail)
                        continue;

                    tail.next = newNode;
                    newNode.prev = tail;

                    tail = newNode;

                    if (tmpSize != size.getAndIncrement()) {
                        System.out.println("updated");
                    }

                    return;
                } finally {
                    tailLock.unlock();
                    queueLock.readLock().unlock();
                }
            } else {
                if (headLock.tryLock()) {
                    try {
                        tail = newNode;
                        head = newNode;

                        size.incrementAndGet();
                        return;
                    } finally {
                        headLock.unlock();
                    }
                }
            }
        }
    }

    T poll() {
        while (true) {
            Node<T> tmpHead = head;
            Node<T> tmpTail = tail;

            if (tmpHead == null)
                return null;

            int tmpSize = size.get();

            if (tmpHead != tmpTail) {
                // queueLock.readLock().lock();
                headLock.lock();

                try {
                    if (tmpHead != head || tmpTail != tail)
                        continue;

                    T result = head.element;

                    head = tmpHead.next;
                    head.prev = null;

                    if (tmpSize == size.getAndDecrement()) {
                        System.out.println("updated");
                    }

                    return result;

                } finally {
                    headLock.unlock();
                }
            } else {
                // queueLock.writeLock().lock();
                if (tailLock.tryLock()) {
                    try {
                        if (tmpHead != head || tmpTail != tail)
                            continue;

                        head = tail = null;
                        size.decrementAndGet();

                        return tmpHead.element;
                    } finally {
                        // queueLock.writeLock().unlock();
                        tailLock.unlock();
                    }
                }
            }
        }
    }

}
