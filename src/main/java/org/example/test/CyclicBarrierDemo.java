package org.example.test;

import java.util.concurrent.*;

import static java.lang.Thread.sleep;

public class CyclicBarrierDemo {
    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(3);

        CyclicBarrier barriers = new CyclicBarrier(3, () -> {
            System.out.println("推倒了一次屏障");
        });

        for (int i = 0; i < 8; i++) {
            int j = i % 3 + 1;
            float k = (float) (i + 1) / 3;
            executor.submit(() -> {
                System.out.printf("我是第%d个到达这个屏障的\n", j);
                try {
                    sleep(500);
                    barriers.await();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                } catch (BrokenBarrierException e) {
                    throw new RuntimeException(e);
                }
            });
            if (k % 1 == 0) {
                int b = (int) k;
                System.out.printf("我们三个合力推到了第%d屏障\n", b);
            }
        }
    }
}
