package org.example.t8;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;

/**
 * 在并发数不是很多的情况下，AtomicLong比LongAdder速度快
 * 相反在并发数很多的情况下，LongAdder更快
 */
@Slf4j(topic = "c.time")
public class LonAdderDemo {
    public static void main(String[] args) {
        AtomicLong atomicLong = new AtomicLong(0L);

        log.debug("AtomicLong 并发开始");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    atomicLong.getAndIncrement();
                }
            }).start();
        }

        while (atomicLong.get() != 100 * 10000) {
        }
        log.debug("AtomicLong 测试结束 结果：" + atomicLong.get());


        LongAdder longAdder = new LongAdder();
        log.debug("LongAdder 并发 开始");
        for (int i = 0; i < 100; i++) {
            new Thread(() -> {
                for (int j = 0; j < 10000; j++) {
                    longAdder.increment();
                }
            }).start();
        }

        while (longAdder.sum() != 100 * 10000) {
        }
        log.debug("LongAdder 测试结束 结果：" + longAdder.sum());

    }
}
