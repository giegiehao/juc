package org.example.t11;

import java.lang.ref.PhantomReference;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 *
 */
public class ThreadLocalDemo {
    public static void main(String[] args) {
        new Thread(() -> {
            // 初始化ThreadLoad对象
            ThreadLocal<MyObject> integerThreadLocal = ThreadLocal.withInitial(() -> new MyObject());
            // 设置和使用
            integerThreadLocal.set(integerThreadLocal.get());

            // 分离初始化
            ThreadLocal<MyObject> integerThreadLocal1 = new ThreadLocal<>();
            if (integerThreadLocal1.get() == null) {
                integerThreadLocal1.set(new MyObject());
            }
            WeakReference<MyObject> myObjectWeakReference = new WeakReference<MyObject>(new MyObject());
            PhantomReference<Object> myObjectPhantomReference = new PhantomReference<Object>(new Object(), new ReferenceQueue<>());
        }).start();
    }

    public static MyObject getObject() {
        return new MyObject();
    }
    
}

class MyObject {

}
