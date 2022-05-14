package com.zhang.juc.interrupt;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.LockSupport;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @auther zzyy
 * @create 2020-07-10 14:05
 */
public class LockSupportDemo
{
    static Object objectLock = new Object();

    static Lock lock = new ReentrantLock();


    public static void main(String[] args)
    {
//        Thread t1 = new Thread(() -> {
//            System.out.println(Thread.currentThread().getName() + "\t" + "---come in");
//            LockSupport.park();
//            LockSupport.park();
//            System.out.println(Thread.currentThread().getName() + "\t" + "---被唤醒");
//        }, "t1");
//        t1.start();
//
//        new Thread(() -> {
//            LockSupport.unpark(t1);
//            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
//            LockSupport.unpark(t1);
//            System.out.println(Thread.currentThread().getName()+"\t"+"---发出通知");
//        },"t2").start();

//        syncWaitNotify();
        lockAwaitSignal();
    }


    static Condition condition = lock.newCondition();


    /**
     *
     */
    public static void lockAwaitSignal()
    {
        new Thread(() -> {
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            lock.lock();
            try
            {
                System.out.println(Thread.currentThread().getName()+"\t"+"---come in");
                condition.await();
                System.out.println(Thread.currentThread().getName()+"\t"+"---被唤醒");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } finally {
                lock.unlock();
            }
        },"t1").start();

        new Thread(() -> {
            lock.lock();
            try
            {
                //唤醒线程
                condition.signal();
                System.out.println(Thread.currentThread().getName()+"\t"+"---发出通知");
            }finally {
                lock.unlock();
            }
        },"t2").start();
    }

    /**
     * 要求t1线程等待2秒 2秒之后t2线程唤醒t1线程
     * 1. 正常情况如下演示没有问题
     * 2. 当去掉同步代码块synchronized时 程序会抛出异常
     * * *  Exception in thread "t1" java.lang.IllegalMonitorStateException at java.lang.Object.wait(Native Method)
     * * *  Exception in thread "t2" java.lang.IllegalMonitorStateException at java.lang.Object.notify(Native Method)
     * 3. 结论
     *  Object类中的wait,notify,notifyAll用于唤醒等待的方法必须在synchronized代码块中执行,否则会抛出异常
     * 4. 将notify放在前面先执行 然后在执行wait等待方法?
     * * *  4.1 程序会一直无法结束
     * * *  4.2  先wait后notify可以正常唤醒 但是先notify后wait不可以正常唤醒
     */
    public static void syncWaitNotify()
    {
        new Thread(() -> {
            //暂停几秒钟线程 先让notify方法执行
//            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            synchronized (objectLock){
                System.out.println(Thread.currentThread().getName()+"\t"+"---come in");
                try {
                    //调用wait方法会释放锁
                    objectLock.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(Thread.currentThread().getName()+"\t"+"---被唤醒");
            }
        },"t1").start();

        //暂停几秒钟线程
        try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }

        new Thread(() -> {
            synchronized (objectLock){
                objectLock.notify();
                System.out.println(Thread.currentThread().getName()+"\t"+"---发出通知");
            }
        },"t2").start();
    }
}