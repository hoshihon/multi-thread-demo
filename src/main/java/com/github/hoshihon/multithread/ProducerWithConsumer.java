package com.github.hoshihon.multithread;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProducerWithConsumer {

    public static class ProducerConsumerQueue<T> {

        private final LinkedList<T> list = new LinkedList<>();

        private int maxSize;

        ProducerConsumerQueue(int maxSize) {
            this.maxSize = maxSize;
        }

        public void push(T obj) throws InterruptedException {
            synchronized (list) {
                System.out.print("produce: " + obj + "\t");
                while (list.size() >= maxSize) {
                    list.wait(); // sleep
                }
                list.push(obj);
                System.out.println("after produce size " + list.size());
                list.notifyAll();
            }
        }

        public List<T> poll(int n) throws InterruptedException {
            List<T> result = new ArrayList<T>();
            synchronized (list) {
                for (int i = 0; i < n; i++) {
                    while (list.isEmpty()) {
                        list.notifyAll();
                        System.out.println("waiting object, needs " + (n - i));
                        list.wait();
                    }

                    T t = list.poll();
                    result.add(t);
                    System.out.println("consume: " + t);
                }
                System.out.println("after consume size " + list.size());
                return result;
            }

        }

        public T poll() throws InterruptedException {
            synchronized (list) {
                while (list.isEmpty()) {
                    list.notifyAll();
                    list.wait();
                }
                T t = list.poll();

                System.out.println("consume: " + t);
                System.out.println("after consume size " + list.size());

                return t;
            }
        }
    }

    public static class Producer implements Runnable {
        ProducerConsumerQueue<Object> list;

        public Producer(ProducerConsumerQueue<Object> list) {
            this.list = list;
        }


        @Override
        public void run() {
            for (; ; ) {
                try {
                    Object obj = new Object();
                    list.push(obj);
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static class Consumer implements Runnable {
        ProducerConsumerQueue<Object> list;
        int n;

        public Consumer(ProducerConsumerQueue<Object> list, int n) {
            this.list = list;
            this.n = n;
        }

        @Override
        public void run() {
            for (; ; ) {
                try {
                    list.poll(n);
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    System.out.println(e.getMessage());
                }
            }
        }
    }

    public static void main(String[] args) {
        ProducerConsumerQueue<Object> list = new ProducerConsumerQueue<>(5);
        Producer producer = new Producer(list);
        Consumer consumer = new Consumer(list, 2);

        ExecutorService executorService = Executors.newFixedThreadPool(2);

        executorService.submit(producer);
        executorService.submit(consumer);
    }

}
