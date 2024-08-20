package org.example.t1;

import lombok.extern.slf4j.Slf4j;

import java.util.Currency;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;

@Slf4j(topic = "c.main")
public class FutureTaskMain {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        FutureTask<Integer> integerFutureTask = new FutureTask<Integer>(() -> {
            log.debug("任务开始");
            Thread.sleep(2000);
            return 2000;
        });

        new Thread(integerFutureTask, "t1").start();
        Integer i = integerFutureTask.get();

        log.debug("任务结果：" + i);
    }
}
