package org.example.t5;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

@Slf4j(topic = "c.test")
public class ParkInterrupt {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.debug("start...");
            LockSupport.park();
            log.debug("interrupted...");
        });
        thread.start();

        sleep(500);
        System.out.println("线程park时的中断值：" + thread.isInterrupted());
        thread.interrupt();
        System.out.println("线程interrupt后的中断值：" + thread.isInterrupted());
    }
}
