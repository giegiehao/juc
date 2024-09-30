package org.example.t8;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.function.LongBinaryOperator;

public class LongAccumulatorDemo {
    public static void main(String[] args) {
        LongAccumulator longAccumulator = new LongAccumulator((x, y) -> {
            long a = x - y;
            return a * x - y;

        }, 1);

        for (int i = 0; i < 10; i++) {
            longAccumulator.accumulate(2);
        }

        System.out.println(longAccumulator.get());
    }
}
