package org.example.t10;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureDemo4 {
    ExecutorService executorService = Executors.newFixedThreadPool(3);
    boolean isTure = true;


    public static void main(String[] args) throws InterruptedException {
        CompletableFutureDemo4 completableFutureDemo4 = new CompletableFutureDemo4();

        completableFutureDemo4.sync();
        completableFutureDemo4.async();

        Thread.sleep(100000);
    }

    /**
     * 不使用async后缀方法
     */
    public void sync() {
        CompletableFuture.runAsync(() -> {
            try {
                Thread.sleep(500000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).thenRun(() -> {
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    /**
     * 使用async后缀方法
     */
    public void async() {
        CompletableFuture.runAsync(() -> {
            try {
                System.out.println("进入异步任务1");
                while (isTure) {
                    if (isTure) {
                        Thread.sleep(1000);
                    }
                }
                int x = 0;
                System.out.println("异步任务已完成1");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, this.executorService).thenRunAsync(() -> {
            try {
                System.out.println("进入异步任务2");
                Thread.sleep(5000);
                System.out.println("异步任务已完成2");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }, this.executorService);
    }
}
