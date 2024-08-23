package org.example.t4;

import lombok.extern.slf4j.Slf4j;

@Slf4j(topic = "c.test")
public class GuardedSuspension {
    public static void main(String[] args) throws InterruptedException {
        GuardedClass guardedClass = new GuardedClass();
        new Thread(() -> {
            try {
                log.debug("getting.....");
                Integer result = guardedClass.getResult(1000);
                log.debug("result={}", result);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }).start();

        Thread.sleep(2000);
        log.debug("set result......");
        guardedClass.setResult(100);
    }
}

class GuardedClass {
    Integer result;

    /**
     * 同步获取数据方法，使用保护性暂停设计模式
     *
     * @param timeout 超时时间
     * @return
     */
    public synchronized Integer getResult(long timeout) throws InterruptedException {
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

    public synchronized void setResult(int x) {
        this.result = x;
        this.notifyAll();
    }
}
