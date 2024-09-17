package org.example.t7;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

public class VolatileNonAtomicTest {
    volatile int res = 0;
    void add() { // 非原子操作，没有synchronized确保原子性，idea提示非原子操作。
        res++;
    }
    public static void main(String[] args) {
        VolatileNonAtomicTest volatileNonAtomicTest = new VolatileNonAtomicTest();
        Thread main = Thread.currentThread();
        for (int i = 0; i < 10; i++) {
            int finalI = i;
            Thread thread = new Thread(() -> {
                for (int j = 0; j < 1000; j++) {
                    try {
                        sleep(2);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                    volatileNonAtomicTest.add();
                }
                if (finalI == 9) {
                    LockSupport.unpark(main);
                }
            });
            thread.start();
            if (i == 9) {
                LockSupport.park();
            }
        }
        System.out.println(volatileNonAtomicTest.res); // 结果不能到10000，证明volatile不能保证原子性，发生了结果覆盖
    }
}
