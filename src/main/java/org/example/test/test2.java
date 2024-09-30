package org.example.test;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

public class test2 {
    public static void main(String[] args) {

        AtomicReferenceFieldUpdater<AtomicFieldClass, MustAtomic> atomicUpdater = AtomicReferenceFieldUpdater.newUpdater(AtomicFieldClass.class, MustAtomic.class, "mustAtomic");
        AtomicFieldClass atomicFieldClass = new AtomicFieldClass(0, 0);

        for (int i = 1; i < 10; i++) {
            MustAtomic mustAtomic = atomicFieldClass.getMustAtomic();
            for (int j = 0; j < 10; j++) {
                int finalI = i;
                new Thread(() -> {
                    boolean b = atomicUpdater.compareAndSet(atomicFieldClass, mustAtomic, new MustAtomic(finalI));
                    if (b) System.out.println("原子属性被修改，版本号为：" + atomicFieldClass.getMustAtomic().getStamp() + "\t旧版本号：" + mustAtomic.getStamp());

                }).start();
            }
        }
    }


}

class AtomicFieldClass {
    public volatile MustAtomic mustAtomic;
    private int stamp;

    public AtomicFieldClass(int fieldStamp, int stamp) {
        this.mustAtomic = new MustAtomic(fieldStamp);
        this.stamp = stamp;
    }

    public MustAtomic getMustAtomic() {
        return mustAtomic;
    }

    public void setMustAtomic(MustAtomic mustAtomic) {
        this.mustAtomic = mustAtomic;
    }

    public int getStamp() {
        return stamp;
    }

    public void setStamp(int stamp) {
        this.stamp = stamp;
    }
}

class MustAtomic {
    private Integer stamp;
    public MustAtomic(int stamp) {
        this.stamp = stamp;
    }

    public Integer getStamp() {
        return stamp;
    }
}