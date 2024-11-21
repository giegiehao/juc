package org.example.test;

import java.util.UUID;

public class test1 {

    public static void main(String[] args) {
        int x = 1;
        System.out.println(x&(~x));
        System.out.println(2 | 1);
        System.out.println(3 & 3);
        System.out.println(~3);
        System.out.println(~3 & 3);
        System.out.println(3 & 2);
        System.out.println(UUID.randomUUID());
    }
}
