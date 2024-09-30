package org.example.t7;

import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.Thread.sleep;

public class VolatileSeeTest {
    static boolean flag = true; // 不能保证及时提交内存和刷新缓存

    //    static volatile boolean flag = true; // volatile可以保证flag被修改后，下一次从内存中刷新最新值到缓存中（可见性）
    public static void main(String[] args) {

        new Thread(() -> {
            System.out.println("start......");
            while (flag) {
                try {
                    sleep(500);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("end........");
        }).start();

        try {
            sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        flag = false;
        System.out.println("set end....");
    }
}
