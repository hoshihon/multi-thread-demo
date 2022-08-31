package com.github.hoshihon.basic;

import java.util.Map;
import java.util.Random;

public class BasicJavaPractice {

    public static class Letter {
        String ch;
        public void post(Letter letter) {
            letter.ch = "post complete";
        }
    }

    //cast
    public static void casting() {
        int i = 200;
        long iLong = (long) i;

        System.out.println("cast long i = " + iLong);
        i = (int) iLong;
        System.out.println("cast int i = " + i);

        iLong = i;
        System.out.println("cast long i = " + iLong);


    }


    //object complete
    public static void bool() {
        Random random = new Random(50);
        int a = random.nextInt(100);
        int b = random.nextInt(100);

        System.out.println("a = " + a);
        System.out.println("b = " + b);
        System.out.println("a > b = " + (a > b));
        System.out.println("a < b = " + (a < b));
        System.out.println("a == b = " + (a == b));
        System.out.println("a != b = " + (a != b));

    }

    // object trans
    public static void postLetter() {
        Letter letter = new Letter();

        letter.ch = "post";
        System.out.printf("before letter post: %s ", letter.ch);
        letter.post(letter);
        System.out.printf("after letter post: %s", letter.ch);
    }

    public static boolean condition() {
        boolean result = Math.random() < 0.99;
        System.out.println("result:" + result);
        return result;
    }

    public static void examplefor() {
        for (int i = 1, j = i + 5; i < 5; i++, j = i + 2) {
            System.out.println("i = " + i + " j = " + j);
        }
    }

    public static void exampleFroeach() {
        Random random = new Random(50);
        float f[] = new float[10];
        for (int i = 0; i < 10; i++) {
            f[i] = random.nextFloat();

        }
        //float Froeach
        for (float x : f) {
            System.out.println(x);
        }
        //String Froeach
        for (char c : "An afrian".toCharArray())
            System.out.print(c + " ");

    }

    public static void exampleBreakContinue() {
        System.out.println("forloop break continue");
        for (int i = 0; i < 100; i += 2) {
//            break loopend
//            continue over element
            if (i == 42) {
                System.out.println(" \n" + i + " loop break");
                break;
            }
            if (i % 8 != 0) {
                continue;
            }
            System.out.print(i + " ");
        }
        System.out.println();
    }
    public static void exampleLabel(){
//        标签label作用于迭代语句之前
//        label: outer, inner
        int i = 0;
        outer:
        for(; true ;){
            inner:
            for (; i < 10 ; i++){
                System.out.println("i = " + i);
                if(i == 2){
                    System.out.println("continue");
                    continue;
                }
                if(i == 3){
                    System.out.println("break");
                    i++;
                    break;
                }
                if(i == 7){
                    System.out.println("continue outer");
                    i++;
                    continue outer;
                }
                if (i == 8){
                    System.out.println("break outer");
                    break outer;
                }
                for (int j = 0; j < 5; j++){
                    if (j == 3){
                        System.out.println("continue inner");
                        continue inner;
                    }
                }
            }
        }
    }


    public static void main(String[] args) {
//        postLetter();
//        bool();
//        casting();

//        while (condition()) {System.out.println("Inside 'while'");}
//        System.out.println("Exited 'while'");
//        examplefor();
//        exampleFroeach();
//        exampleBreakContinue();
        exampleLabel();
    }

}
