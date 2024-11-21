package org.example.test;

import java.util.concurrent.locks.StampedLock;

import static java.lang.Thread.sleep;

public class StampedLockTest {
    private StampedLock stampedLock;

    public StampedLockTest() {
        this.stampedLock = new StampedLock();
    }

    public static void main(String[] args) throws InterruptedException {
        StampedLockTest stampedLockTest = new StampedLockTest();

        new Thread(() -> {
            stampedLockTest.write();
        }).start();

//        sleep(100);

//
//        for (int i = 0; i < 11; i++) {
//            new Thread(() -> {
//                stampedLockTest.read();
//            }).start();
//        }
//        new Thread(() -> {
//            stampedLockTest.read();
//        }).start();

        for (int i = 0; i < 11; i++) {
            sleep(100);
            new Thread(() -> {


                stampedLockTest.read();
            }).start();
        }
//        stampedLockTest.read();

//        sleep(1100);
//        long l = stampedLockTest.stampedLock.tryOptimisticRead();
//        System.out.println(l);
//
//        stampedLockTest.read();
    }

    public void read() {
        long stamp = this.stampedLock.tryOptimisticRead();
        System.out.println("read:" + stamp);
        if(!stampedLock.validate(stamp)){              // 校验
            long lockStamp = stampedLock.readLock();               // 获取读锁
            try {
                //重新读取
                System.out.println("获取read独占锁");
                sleep(500);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                stampedLock.unlock(lockStamp);              // 释放悲观锁
            }

        }
    }

    public void write() {
        long l = this.stampedLock.writeLock();
        System.out.println("write:" + l);
        try {
            sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            this.stampedLock.unlockWrite(l);
        }
    }
}
