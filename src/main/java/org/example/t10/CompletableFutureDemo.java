package org.example.t10;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CompletableFutureDemo {
    ExecutorService threadPool = Executors.newFixedThreadPool(2);

    public static void main(String[] args) throws InterruptedException {
        CompletableFutureDemo completableFutureDemo = new CompletableFutureDemo();

//        for (int i = 0; i < 3; i++) {
//            Thread.sleep(100);
            completableFutureDemo.test1();
//            completableFutureDemo.test1();

//        }

        System.out.println("main sleep");
        Thread.sleep(1000);
        System.out.println("main sleep end");
    }

    public CompletableFutureDemo() {
    }

    /**
     * 测试异步任务完成后，whenComplete是在任务线程还是调用线程（主线程）执行
     * 测试结果：
     * 异步任务不影响主线程继续执行，且当异步任务完成时，异步任务线程会继续处理任务完成的后续任务
     * ！！！但是在任务执行很快的情况下，后续任务出现了执行者是main线程的情况
     */
    public void test1() {
        CompletableFuture.supplyAsync(() -> {
            System.out.println("async come in----------");
            System.out.println(Thread.currentThread().getName());
//            System.out.println(Thread.currentThread().getId());
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException e) {
//                throw new RuntimeException(e);
//            }
            return ' ';
        }).thenAcceptAsync((u) -> {
            System.out.println("complete---------");
            System.out.println(Thread.currentThread().getName());
//            try {
//                Thread.sleep(2000);
//            } catch (InterruptedException ex) {
//                throw new RuntimeException(ex);
//            }
        }).thenApplyAsync((u) -> {
            System.out.println(Thread.currentThread().getName());

            return null;
        });
    }
}
