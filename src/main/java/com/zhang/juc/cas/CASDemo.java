package com.zhang.juc.cas;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @auther zzyy
 * @create 2021-03-17 15:13
 */
public class CASDemo
{
    public static void main(String[] args)
    {
        AtomicInteger atomicInteger = new AtomicInteger(5);
        System.out.println(atomicInteger.get());


        System.out.println(atomicInteger.compareAndSet(5, 308)+"\t"+atomicInteger.get());

        System.out.println(atomicInteger.compareAndSet(5, 3333)+"\t"+atomicInteger.get());
    }

}


class Incre{
    public static void main(String[] args) throws InterruptedException {
        CountDownLatch countDownLatch = new CountDownLatch(2000);
        AtomicInteger atomicInteger = new AtomicInteger();
        new Thread(()->{
            System.out.println("1 start-----");
            for (int i = 0; i < 1000 ; i++) {
                countDownLatch.countDown();
//                getAndIncrement()方法是返回当前值并在原值上新增1
//                atomicInteger.getAndIncrement();
                // incrementAndGet()方法是先新增值,然后返回更新后的值
                atomicInteger.incrementAndGet();
            }
        },"a").start();

        new Thread(()->{
            System.out.println("2 start----");
            for (int i = 0; i < 1000 ; i++) {
                countDownLatch.countDown();
                atomicInteger.incrementAndGet();
            }
        },"b").start();
        //阻塞主线程 直到countDownLatch计数器为0
        countDownLatch.await();
        System.out.println(atomicInteger.intValue());
    }
}
