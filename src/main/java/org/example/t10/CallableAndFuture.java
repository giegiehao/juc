package org.example.t10;

import java.util.concurrent.*;

public class CallableAndFuture<T> {
    public static void main(String[] args) throws Exception {
        Callable<Integer> callable = new Callable<>() {
            @Override
            public Integer call() throws Exception {
                int x = 0;
                return ++x;
            }
        };
        CallableAndFuture<Integer> main = new CallableAndFuture<>();

        Integer i = main.testCall(callable);
        System.out.println(i);

        Integer future = main.getFuture(new FutureTask<>(callable));
        System.out.println(future);
    }

    /**
     * 调用call并接收返回值， 直接拿到处理结果
     * @param callable
     * @return
     * @throws Exception
     */
    public T testCall(Callable<T> callable) throws Exception {
        return callable.call();
    }

    /**
     * 使用FutureTask异步调用任务，并且获取结果
     * @param task
     * @return
     * @throws ExecutionException
     * @throws InterruptedException
     */
    public Integer getFuture(FutureTask<Integer> task) throws ExecutionException, InterruptedException {
        new Thread(task).start();
        return task.get();
    }
}
