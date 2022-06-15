package com.zhang.juc.test;

import org.openjdk.jol.info.ClassLayout;

public class Demo03 {
    public static void main(String[] args) {
        Object o = new Object();
        //十进制hashcode
        System.out.println(o.hashCode());
        //16进制hashcode
        System.out.println(Integer.toHexString(o.hashCode()));
        //2进制hashcode
        System.out.println(Integer.toBinaryString(o.hashCode()));

        System.out.println(ClassLayout.parseInstance(o).toPrintable());
    }
}
