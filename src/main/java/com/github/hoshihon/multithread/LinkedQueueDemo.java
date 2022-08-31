package com.github.hoshihon;

import java.util.Random;
import java.util.concurrent.*;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class LinkedQueueDemo {

    public static void main(String[] args) {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        readWriteLock.readLock().lock();

        readWriteLock.writeLock().lock();

        System.out.println("write locked");
    }

    static void multiThreadTest() {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch countDownLatch = new CountDownLatch(8);
        ExecutorService executor = new ThreadPoolExecutor(0, 8, 0, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 4; i++) {
            executor.execute(() -> {
                try {
                    startLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                Random random = new Random();
                for (int j = 0; j < 10000000; j++) {
                    queue.push(random.nextInt());
                }

                countDownLatch.countDown();
            });
        }

        for (int i = 0; i < 4; i++) {
            executor.execute(() -> {
                try {
                    startLatch.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

                Random random = new Random();
                for (int j = 0; j < 5000000; ) {
                    if (queue.poll() != null) j++;
                }

                countDownLatch.countDown();
            });
        }

        startLatch.countDown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        if (queue.size() != 20000000) {
            System.out.println(queue.size());
            throw new IllegalStateException();
        }
    }

    static void validate() {
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>();

        if (queue.poll() != null) throw new IllegalStateException();

        for (int i = 0; i < 10; i++) {
            queue.push(i);
        }

        for (int i = 0; i < 10; i++) {
            if (queue.poll() != i) {
                throw new IllegalStateException();
            }
        }

        if (queue.poll() != null) throw new IllegalStateException();
    }

}
