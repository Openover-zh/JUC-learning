package com.zhang.juc.test;

public class demo02 {
    Object o = new Object();
    public void m1(){
        synchronized (o){
            System.out.println("----------hello");
            throw new RuntimeException("123");
        }
    }
}
