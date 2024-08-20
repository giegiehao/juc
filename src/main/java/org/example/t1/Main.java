package org.example.t1;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLOutput;

@Slf4j(topic = "c.t1")
public class Main {
    public static void main(String[] args) {
        int x = 1;

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("任务和线程分离");
                System.out.println(x);
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();

        Thread thread1 = new Thread(() -> {
            System.out.println("匿名内部任务类(Lambda)");
            System.out.println(x);
        });
        thread1.start();

        Thread thread2 = new Thread(() -> System.out.println("重写thread执行任务"));
        thread2.start();

    }
}
