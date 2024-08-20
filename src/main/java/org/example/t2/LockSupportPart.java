package org.example.t2;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.locks.LockSupport;

@Slf4j(topic = "c.test")
public class LockSupportPart {
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            log.debug("run~");
            LockSupport.park();
            log.debug("unpark");
            log.debug("interrupt: " + Thread.interrupted());
        });

        thread.start();
        Thread.sleep(3000);
        thread.interrupt();
    }
}
