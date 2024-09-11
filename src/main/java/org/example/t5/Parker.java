package org.example.t5;

import java.util.concurrent.locks.LockSupport;

import static java.lang.Thread.sleep;

public class Parker {
    static final Object lock = new Object();
    static String blocker = "Im blocker";
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            System.out.println("start");
            synchronized (lock) {
                try {
                    System.out.println("wait");
                    lock.wait();
                    System.out.println("unwait");
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("park");
            LockSupport.park(blocker);
            System.out.println("unpark");
        });
        thread.start();
        sleep(1000);
        synchronized (lock) {
            System.out.println("park之前的blocker");
            Object blocker = LockSupport.getBlocker(thread);
            System.out.println(blocker);
            lock.notifyAll();
        }
        sleep(1000);
        System.out.println("park之后的blocker");
        Object blocker = LockSupport.getBlocker(thread);
        System.out.println(blocker);

        LockSupport.unpark(thread);
        System.out.println("unpark之后的blocker");
        Object blocker1 = LockSupport.getBlocker(thread);
        System.out.println(blocker1);
    }
}
