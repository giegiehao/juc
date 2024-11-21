package org.example.t10;

import ch.qos.logback.core.encoder.JsonEscapeUtil;

import java.util.concurrent.CompletableFuture;

/**
 * 结果合并
 * 合并两个任务结果，返回最终处理后的结果
 */
public class CompletableFutureDemo2 {
    public static void main(String[] args) {
        CompletableFuture<Integer> integerCompletableFuture = CompletableFuture.supplyAsync(() -> {
            int x = 0;
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < 10; i++) {
                x += i;
            }
            return x;
        }).thenCombine(CompletableFuture.supplyAsync(() -> {
            int y = 0;
            for (int i = 0; i < 10; i++) {
                y += i;
            }
            return y;
        }), (x, y) -> x - y);

        System.out.println(integerCompletableFuture.join()); // get/join是同步的
        System.out.println("任务已结束");
    }
}
