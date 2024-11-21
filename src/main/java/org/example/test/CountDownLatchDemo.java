package org.example.test;

import java.util.concurrent.CountDownLatch;

public class CountDownLatchDemo {
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2);

        new Thread(() -> {
            System.out.println("执行任务一");
            countDownLatch.countDown(); // 进行一次倒数 state = 2 - 1 = 1
        }).start();
        new Thread(() -> {
            System.out.println("执行任务二");
            countDownLatch.countDown(); // 进行一次倒数 state = 1 - 1 = 0
        }).start();


        countDownLatch.await(); // 阻塞等待倒数器数到0
        System.out.println("倒数器倒数结束，任务开始执行");
    }
}
