package org.example.t9;

import lombok.extern.slf4j.Slf4j;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * 实现自定义线程池，执行任务
 * 1. 任务队列
 * 2. 线程池管理线程执行任务
 * 功能：
 * - 任务队列满时的任务处理
 * - 任务等待超时时间
 */
@Slf4j(topic = "c.main")
public class TestPool {
    public static void main(String[] args) {
        MyThreadPool myThreadPool = new MyThreadPool(2, 10, TimeUnit.SECONDS, 10);
        for (int i = 0; i < 15; i++) {
            int finalI = i;
            Runnable task = () -> log.debug(Thread.currentThread().getName() + "完成任务" + finalI);
            myThreadPool.execute(task);
            log.debug("成功提交任务：" + i + "内存编号：" + task);
        }
    }
}

/**
 * 自定义线程池，管理线程生命周期、管理线程执行任务
 */
@Slf4j(topic = "c.threadPool")
class MyThreadPool {
    private int coreSize; // 线程池大小
    private long timeout; // 线程无任务时超时时间，超过这个时间没有任务，销毁线程
    private TimeUnit timeUnit;

    private TaskQueue<Runnable> taskQueue;
    private HashSet<MyThread> workers = new HashSet<>();
    ;

    public MyThreadPool(int coreSize, long timeout, TimeUnit timeUnit, int taskSize) {
        this.coreSize = coreSize;
        this.timeout = timeout;
        this.timeUnit = timeUnit;
        this.taskQueue = new TaskQueue<>(taskSize);
    }

    public void execute(Runnable task) {
        if (workers.size() < coreSize) {
            MyThread myThread = new MyThread(task);
            workers.add(myThread);
            myThread.start();
            log.debug("新建核心线程执行任务{}", task);
        } else {
            taskQueue.putTask(task);
            log.debug("核心线程数上限，{}进入任务队列", task);
        }
    }

    class MyThread extends Thread {
        Runnable task;

        public MyThread(Runnable task) {
            this.task = task;
        }

        @Override
        public void run() {
            try {
                if (timeout == 0) {
                    do {
                        task.run();
                    } while ((task = taskQueue.takeTask()) != null); // 无限等待 不回收核心线程
                } else {
                    do {
                        task.run();
                    } while ((task = taskQueue.takeTask(timeout, timeUnit)) != null); // 有限等待， 超时后回收线程。
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            synchronized (workers) {
                workers.remove(this); // 没有任务，退出线程池，等待销毁
                log.debug("销毁线程{}", Thread.currentThread().getName());
            }
        }
    }
}


class TaskQueue<T> {
    // 任务队列
    private Deque<T> taskQueue = new ArrayDeque<>();
    private ReentrantLock lock = new ReentrantLock();
    private Condition fullWaitSet = lock.newCondition(); // 队列满时的等待
    private Condition emptyWaitSet = lock.newCondition(); // 任务为空时等待
    //    private Long timeout; // 等待的时间
    private int queueSize; // 等待队列的大小

    public TaskQueue(int size) {
//        this.timeout = timeUnit.convert(timeout, TimeUnit.NANOSECONDS);
        this.queueSize = size;
    }

    /**
     * 添加一个任务到任务队列中 添加不上阻塞线程
     *
     * @param task
     * @return
     */
    public boolean putTask(T task) {
        lock.lock();
        try {
            while (taskQueue.size() == queueSize) {
                fullWaitSet.await();
            }
            taskQueue.addLast(task);
            emptyWaitSet.signal(); // 有任务可以执行
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
        return true;
    }

    /**
     * 从任务队列获取一个任务 无限等待
     *
     * @return
     */
    public T takeTask() {
        lock.lock();
        try {
            while (taskQueue.isEmpty()) {
                emptyWaitSet.await();
            }
            T t = taskQueue.removeFirst();
            fullWaitSet.signal(); // 任务队列可以放了
            return t;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }

    /**
     * 从任务队列中获取一个任务 有限等待
     *
     * @param timeout
     * @param timeUnit
     * @return
     */
    public T takeTask(long timeout, TimeUnit timeUnit) {
        timeout = timeUnit.toNanos(timeout);
        lock.lock();
        try {
            while (taskQueue.isEmpty()) {
                if (timeout > 0) {
                    timeout = emptyWaitSet.awaitNanos(timeout);
                } else return null;
            }
            T t = taskQueue.removeFirst();
            fullWaitSet.signal();
            return t;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
        }
    }
}
