package org.example.test;

import static java.lang.Thread.sleep;

public class Test3 {
    public volatile TestClass testClass;
    public static void main(String[] args) {
        Test3 test3 = new Test3();
        test3.run();
    }

    private void run() {
        new Thread(() -> {
            try {
                sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            testClass = new TestClass(1);
        }).start();

        new Thread(() -> {
            while (testClass == null) {
            }
            System.out.println("l = " + testClass.testClass2.x);
        }).start();
    }
}

class TestClass {
    public long l;
    public TestClass2 testClass2;

    public TestClass(long l) {
        this.l = l;
        this.testClass2 = new TestClass2(l);
    }
}

class TestClass2 {
    public long x;

    public TestClass2(long x) {
        this.x = x;
    }
}
