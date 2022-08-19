package com.github.hoshihon.multithread;

import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.Random;

public class producer_consumer {

    public static class Factory {
        private int max_size = 20;
        private LinkedList goods_list = new LinkedList();
        private String name = "";

        Factory(String name, int max_size){
            this.name = name;
            this.max_size = max_size;
        }

        public void produce(int num){
            synchronized (goods_list){
                while (goods_list.size() + num >= max_size){
                        System.out.println("预计生产 " + name +" 数量: " + num
                            + " 总库存量:  " +goods_list.size() );
                    System.out.println("生产等待阻塞");
                    try {
                        goods_list.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i = 0; i < num; i++){
                        goods_list.add(new Object());
                    }
                    System.out.println("以生产" + name + "数量: " + num
                            + " 现库存量: " +goods_list.size() );
                    goods_list.notifyAll();
                }
            }
        }
        public void consume(int num){
            synchronized (goods_list){
                while (num > goods_list.size()){
                    System.out.println("预计消费 " + name +" 数量: " + num
                            + "总库存量: " +goods_list.size() );
                    System.out.println("消费等待阻塞");
                    try {
                        goods_list.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    for (int i=0; i<num; i++){
                        goods_list.remove();
                    }
                    System.out.println("已消费" + name + "数量: " + num
                            + " 现库存量: " +goods_list.size() );
                    goods_list.notifyAll();
                }
            }
        }


    }

    public static class Producer implements Runnable{
        public Factory factory;

        public Producer(Factory factory){
            this.factory = factory;
        }
        @Override
        public void run() {
            Random random = new Random();
            factory.produce(random.nextInt(10));
        }
    }
    public static class Consumser implements Runnable{
        public Factory factory;
        public Consumser(Factory factory){
            this.factory = factory;
        }
        @Override
        public void run() {
            Random random = new Random();
            factory.consume(random.nextInt(10));
        }

    }
    public static void main(String[] args) {
        Factory factory_crystals = new Factory("宝晶石", 20);

        Producer producer_crystals = new Producer(factory_crystals);
        Consumser consumer_crystals = new Consumser(factory_crystals);

        for (int i = 0; i < 5; i++){
            new Thread(producer_crystals, "小福" + i).start();
        }
        for (int i = 0; i < 5; i++){
            new Thread(consumer_crystals, "骑空士" + i).start();
        }
    }

}
