package org.example.t4;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test")
public class WaitNotifyTest {
    static Boolean has_banana = false;
    static Boolean has_apple = false;
    static final Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {
        new Thread(() -> {
            log.debug("start");
            synchronized (lock) {
                log.debug("有没有香蕉？");
                while (!has_banana) {
                    log.debug("没有，不干");
                    try {
                        lock.wait();
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
                log.debug("有，干活");
            }
        }, "小南").start();

        new Thread(() ->
        {
            log.debug("start");
            synchronized (lock) {
                do {
                    log.debug("有没有苹果？");
                    if (!has_apple) {
                        log.debug("没有，不干");
                        try {
                            lock.wait();
                        } catch (InterruptedException e) {
                            throw new RuntimeException(e);
                        }
                    } else log.debug("有，干活");
                } while (!has_apple);
            }
        }, "小红").

                start();

        Thread.sleep(500);
        //wait和notify都必须获得该锁后使用
        synchronized (lock) {
            has_banana = true;
            lock.notifyAll();
            lock.wait(1000);
            has_apple = true;
            lock.notifyAll();
        }
    }
}
