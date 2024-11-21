package org.example.t11;

import static java.lang.Thread.sleep;

/**
 * 测试公共类中的ThreadLocal是否可以被其它线程获得
 * 公共类中的ThreadLocal不可以被其他线程共享，但是线程可以获得自己线程的该ThreadLocal值
 * 原因：ThreadLocalMap的entry数组下标是以对象的hashcode值来计算的，只要对象相同，就可以找到该线程下的ThreadLocal对象
 */
public class ThreadLocalDemo1 {
    private LocalCounter localCounter;

    public ThreadLocalDemo1() {
        this.localCounter = new LocalCounter();
    }

    public static void main(String[] args) throws InterruptedException {
        ThreadLocalDemo1 threadLocalDemo = new ThreadLocalDemo1();
        Thread thread1 = new Thread(() -> {
            Counter counter = threadLocalDemo.localCounter.get();
            ++counter.count; // 1
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(counter.threadName + ":" + counter.count);
        });
        Thread thread2 = new Thread(() -> {
            Counter counter = threadLocalDemo.localCounter.get();
            counter.count += 2; // 2
            counter.count += 2; // 4
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(counter.threadName + ":" + counter.count);
        });
        Thread thread3 = new Thread(() -> {
            LocalCounter localCounter1 = new LocalCounter();
            Counter counter = threadLocalDemo.localCounter.get();
            counter.count += 3; // 3
            localCounter1.get().count += 4; //4
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(counter.threadName + ":" + counter.count);
            System.out.println(localCounter1.get().threadName + ":" + localCounter1.get().count);
        });
        Thread thread4 = new Thread(() -> {
            Counter counter = threadLocalDemo.localCounter.get();
            ++counter.count; // 1
            threadLocalDemo.update();
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            System.out.println(counter.threadName + ":" + threadLocalDemo.localCounter.get().count);
        });

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        sleep(1000);
        System.out.println(threadLocalDemo.localCounter.get().count);

    }

    public void update() {
        this.localCounter = new LocalCounter();
    }
}

class Counter {
    int count;
    String threadName = Thread.currentThread().getName();
}

class LocalCounter extends ThreadLocal<Counter>{
    @Override
    protected Counter initialValue() {
        return new Counter();
    }
}
