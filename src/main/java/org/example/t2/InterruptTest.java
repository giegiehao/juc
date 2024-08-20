package org.example.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j(topic = "c.interrpt")
public class InterruptTest {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.debug("sleeping~");
            try {
                TimeUnit.SECONDS.sleep(5);
            } catch (InterruptedException e) {
                // 打断睡眠，抛出异常，不抛出则继续运行
                log.debug("线程被打断");
            }
            log.debug("线程正常结束");
        });

        Thread thread1 = new Thread(() -> {
            log.debug("wait thread~");
            try {
                thread.join();
            } catch (InterruptedException e) {
                log.debug("打断等待");
            }
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            log.debug("不等了，回家吃饭");
        });

        log.debug("thread start");
        thread.start();
        thread1.start();

        TimeUnit.SECONDS.sleep(1);
        thread1.interrupt();
        TimeUnit.MILLISECONDS.sleep(100);
        log.debug("t1 interrupt:" + thread1.isInterrupted());
    }
}
