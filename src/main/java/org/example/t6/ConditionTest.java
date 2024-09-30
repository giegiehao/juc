package org.example.t6;

import lombok.extern.slf4j.Slf4j;
import sun.misc.Unsafe;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import static java.lang.Thread.sleep;

@Slf4j(topic = "c.test")
public class ConditionTest {
    public static void main(String[] args) throws InterruptedException {
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();

        new Thread(() -> {
            lock.lock();
            try {
                sleep(1000);
                log.debug("线程在条件队列中等待");
                condition.awaitUninterruptibly();
                log.debug("被唤醒");
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }finally {
                lock.unlock();
            }
        }, "thread1").start();

        sleep(1500);
        lock.lock();
        try {
            condition.signalAll();
        }finally {
            lock.unlock();
        }
    }
}
