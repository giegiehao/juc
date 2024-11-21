package org.example.t10;

import java.util.concurrent.CompletableFuture;

public class CompletableFutureDemo3 {
    public static void main(String[] args) {
        CompletableFutureDemo3 completableFutureDemo3 = new CompletableFutureDemo3();

        completableFutureDemo3.testException();
    }

    /**
     * 测试Handle的异常传递
     * 1.异常被下一个任务处理后，是否会传递到下下个任务？
     * 答：不会。下一个任务可以对异常做出一些处理，但是异常需要手动抛出才可以传递到下一个任务，直到抛给到异常处理才会被它处理，如果某个任务中断了异常抛出，则异常处理也不会接收到异常。
     * 2.如果多个任务出现异常如何处理？
     * 答：每次异常被传递后都会在异常堆栈信息中加上新的异常信息，直到被异常处理捕捉，获取到整个异常堆栈。
     */
    public void testException() {
        CompletableFuture.runAsync(() -> {
            System.out.println("asyncTaskA is start----------");
            System.out.println("throw exception----------");
            int x = 10 / 0; // 抛出一个异常
        }).handle((r, e) -> {
            System.out.println("asyncTaskB is start----------");
            try {
                System.out.println(e == null);
//                e.printStackTrace();
            } catch (Exception exception) {
                System.out.println("不可以捕捉到上一个任务的线程");
            }
            if (e != null) try {
                throw e;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }).handle((r, e) -> {
            System.out.println("asyncTaskC is start----------");
            System.out.println(e == null);
//            e.printStackTrace();
            if (e != null) try {
                throw e;
            } catch (Throwable ex) {
                throw new RuntimeException(ex);
            }
            return null;
        }).exceptionally((e) -> {
            System.out.println("exception handle----------");
            System.out.println(e == null);
            e.printStackTrace();
            System.out.println("----------------------");
            return null;
        });
    }
}
