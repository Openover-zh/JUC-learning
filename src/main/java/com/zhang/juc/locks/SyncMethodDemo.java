package com.zhang.juc.locks;

public class SyncMethodDemo {
    Object o = new Object();
    public void m1(){
        synchronized (o){
            System.out.println("1111111-----");
            m2();
        }
    }

    public void m2(){
        synchronized (o){
            System.out.println("22222222-----");
            m3();
        }
    }
    public void m3(){
        synchronized (o){
            System.out.println("33333333----");
        }
    }

    public static void main(String[] args) {
        new Thread(()->new SyncMethodDemo().m1());
    }
}
