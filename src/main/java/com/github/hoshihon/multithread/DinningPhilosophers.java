package com.github.hoshihon.multithread;

import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

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
                synchronized (left) {
                    System.out.printf("哲学家 %d 拿到左手筷子%n", id);

                    try {
                        int millis = 1000 + random.nextInt(1000);
                        System.out.printf("哲学家 %d 等待 %d 毫秒%n", id, millis);
                        Thread.sleep(millis);
                    } catch (InterruptedException e) {
                        System.out.println(e.getMessage());
                        return;
                    }

                    synchronized (right) {
                        System.out.printf("哲学家%d拿到右手筷子%n", id);
                        System.out.printf("哲学家%d进餐%n", id);

                        try {
                            int millis = 1000 + random.nextInt(1000);
                            System.out.printf("哲学家 %d 等待 %d 毫秒%n", id, millis);
                            Thread.sleep(2000);
                        } catch (InterruptedException e) {
                            System.out.println(e.getMessage());
                            return;
                        }
                    }
                }
            }
        }

    }
}
