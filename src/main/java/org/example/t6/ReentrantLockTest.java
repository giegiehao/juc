package org.example.t6;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.currentThread;
import static java.lang.Thread.sleep;

public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    try {
                        lock.tryLock(1, TimeUnit.SECONDS);
                        //临界区
                        sleep(500);
                        System.out.println(currentThread().getName() + "抢到锁");
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    } finally {
                        //确保释放锁
                        lock.unlock();
                    }
                }
            }
        };
        new Thread(runnable, "线程一").start();
        new Thread(runnable, "线程二").start();

    }
}
