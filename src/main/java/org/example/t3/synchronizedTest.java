package org.example.t3;

import lombok.extern.slf4j.Slf4j;

import java.util.Hashtable;

@Slf4j(topic = "c.sync")
public class synchronizedTest {
    public static void main(String[] args) {
        Thread thread = new Thread(() -> {
            // 静态方法只能使用字节码对象（静态）
            synchronized (synchronizedTest.class) {
                log.debug("main");
            }
        });
    }
}

@Slf4j(topic = "c.room")
class Room {
    private Integer count;
    public synchronized void jia() {
        count++;
    }

    public synchronized void jiang() {
        count--;
    }

    public synchronized static void test() {
        log.debug("静态方法使用类字节码作为锁对象");
    }
}

