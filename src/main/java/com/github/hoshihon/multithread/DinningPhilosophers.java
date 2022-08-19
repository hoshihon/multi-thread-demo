package com.github.hoshihon.multithread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.locks.ReentrantLock;

public class DinningPhilosophers {

    public static void main(String[] args) {
        Chopstick chopstick1 = new Chopstick();
        Chopstick chopstick2 = new Chopstick();
        Chopstick chopstick3 = new Chopstick();
        Chopstick chopstick4 = new Chopstick();

        Philosopher philosopher1 = new Philosopher(1, chopstick1, chopstick2);
        Philosopher philosopher2 = new Philosopher(2, chopstick2, chopstick3);
        Philosopher philosopher3 = new Philosopher(3, chopstick3, chopstick4);
        Philosopher philosopher4 = new Philosopher(4, chopstick4, chopstick1);

        ExecutorService executorService = Executors.newFixedThreadPool(4);

        executorService.execute(philosopher1);
        executorService.execute(philosopher2);
        executorService.execute(philosopher3);
        executorService.execute(philosopher4);

        executorService.shutdown();
    }

    public static class Chopstick {

        ReentrantLock lock = new ReentrantLock();

    }

    public static class Philosopher implements Runnable {

        private int id;

        private Chopstick left;

        private Chopstick right;

        public Philosopher(int id, Chopstick left, Chopstick right) {
            this.id = id;
            this.left = left;
            this.right = right;
        }

        @Override
        public void run() {
            Random random = new Random();

            for (; ; ) {
                if (id % 2 == 0) {
                    haveDinner(random, left, right, "左", "右");
                } else {
                    haveDinner(random, right, left, "右", "左");
                }
            }
        }

        private void haveDinner(Random random, Chopstick first, Chopstick second, String firstHand, String secondHand) {
            if (first.lock.tryLock()) {
                try {
                    System.out.printf("哲学家 %d 拿到%s手筷子%n", id, firstHand);

                    try {
                        int millis = 1000 + random.nextInt(1000);
                        System.out.printf("哲学家 %d 等待 %d 毫秒%n", id, millis);
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                    }

                    if (second.lock.tryLock()) {
                        try {
                            System.out.printf("哲学家%d拿到%s手筷子%n", id, secondHand);
                            System.out.printf("哲学家%d进餐%n", id);

                            try {
                                int millis = 1000 + random.nextInt(1000);
                                System.out.printf("哲学家 %d 等待 %d 毫秒%n", id, millis);
                                Thread.sleep(2000);
                            } catch (InterruptedException e) {
                                System.out.println(e.getMessage());
                            }
                        } finally {
                            second.lock.unlock();
                        }
                    } else {
                        System.out.printf("哲学家%d未获取到%s手筷子%n", id, secondHand);
                        System.out.printf("哲学家%d放弃进餐%n", id);
                    }
                } finally {
                    first.lock.unlock();
                }
            } else {
                System.out.printf("哲学家%d未获取到%s手筷子%n", id, firstHand);
                System.out.printf("哲学家%d放弃进餐%n", id);
            }
        }

    }
}
