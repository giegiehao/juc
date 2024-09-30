package org.example.t8;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class CASDemo {

    AtomicInteger mark = new AtomicInteger();

    public static void main(String[] args) {
        CASDemo casDemo = new CASDemo();
        new Thread(() -> {
            casDemo.park();
            // 模拟任务
            try {
                System.out.println("开始任务");
                sleep(1000);
                System.out.println("任务结束");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            casDemo.unpark();
        }).start();

        new Thread(() -> {
            System.out.println("等待cas");
            casDemo.park();
            System.out.println("cas成功");
            casDemo.unpark();
        }).start();
    }

    public void park() {
        for (;;) {
            if (mark.compareAndSet(0, 1)) break;
        }
    }
    public void unpark() {
        mark.compareAndSet(1, 0);
    }
}
