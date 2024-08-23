package org.example.t4;

import lombok.extern.slf4j.Slf4j;

import java.util.*;

public class GuardedSuspensionDecoupling {
    public static void main(String[] args) throws InterruptedException {
        for (int i = 0; i < 5; i++) {
            new Consumer().start();
        }

        Thread.sleep(1000);
        //针对消费者进行推送
        for (Integer id : DecouplingpObject.getIds()) {
            new Producer(id, id+"").start();
        }
    }
}

//消费者
@Slf4j(topic = "c.consumer")
class Consumer extends Thread {
    @Override
    public void run() {
        int guardedObjectId = DecouplingpObject.createGuardedObject();
        log.debug("等待结果对象{}", guardedObjectId);
        GuardedObject guardedObject = DecouplingpObject.getGuardedObject(guardedObjectId);
        try {
            Object result = guardedObject.getResult(5000);
            log.debug("获得结果对象{}结果：{}", guardedObject, result);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }
}

//生产者
@Slf4j(topic = "c.producer")
class Producer extends Thread {
    private int toId; //消费的对象
    private String meg; //生产内容

    public Producer(int toId, String meg) {
        this.toId = toId;
        this.meg = meg;
    }

    @Override
    public void run() {
        log.debug("服务消费对象：{}", toId);
        GuardedObject guardedObject = DecouplingpObject.getGuardedObject(toId);
        guardedObject.setResult(meg);
    }
}

//解耦对象，使生产者和消费者解耦，统一管理生产者和消费者的一对一关系。
class DecouplingpObject {
    //线程安全的map，保存结果对象的集合
    private static final Hashtable<Integer, GuardedObject> guardedObjectTable = new Hashtable<>();
    private static int count = 1;

    private static synchronized int generateId() {
        return count++;
    }

    public static GuardedObject getGuardedObject(int id) {
        return guardedObjectTable.get(id);
    }

    //生成一个结果对象，并加入表中维护
    public static int createGuardedObject() {
        int id = generateId();
        guardedObjectTable.put(id, new GuardedObject(id));
        return id;
    }

    public static Set<Integer> getIds() {
        return guardedObjectTable.keySet();
    }
}

//结果对象，用来等待和获取结果的，生产者和消费者一对一。
class GuardedObject {
    private final Integer id;
    private Object result;

    public GuardedObject(Integer id) {
        this.id = id;
    }

    /**
     * 同步获取数据方法，使用保护性暂停设计模式
     *
     * @param timeout 超时时间
     * @return
     */
    public synchronized Object getResult(long timeout) throws InterruptedException {
        if (timeout > 0) {
            long start = System.currentTimeMillis();
            long delay = timeout;
            do {
                wait(delay);
            } while (result == null && (delay = start + timeout - System.currentTimeMillis()) > 0);
        } else if (timeout == 0) {
            while (result == null) {
                this.wait();
            }
        }
        return this.result;
    }

    public synchronized void setResult(Object x) {
        this.result = x;
        this.notifyAll();
    }

    public Integer getId() {
        return id;
    }
}
