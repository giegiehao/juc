package org.example.t2;

import static java.lang.Thread.sleep;

public class JoinInterreuptTest {
    static final Object lock = new Object();
    public static void main(String[] args) throws InterruptedException {
        Thread thread = new Thread(() -> {
            synchronized (lock) {
                try {
                    lock.wait();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Thread thread1 = new Thread(() -> {
            try {
                sleep(1000);
                thread.join();
            } catch (InterruptedException e) {
                System.out.println("join被打断");
                throw new RuntimeException(e);
            }
            System.out.println("join被唤醒");
        });
        thread.start();
        thread1.start();
        sleep(1000);

        //无法打断唤醒join中的线程
        synchronized (thread1) {
            thread1.notifyAll();
        }
        //可以被打断
        thread1.interrupt();
    }
}
