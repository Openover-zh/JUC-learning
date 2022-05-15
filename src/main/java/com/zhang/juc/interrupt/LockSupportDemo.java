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

        lockSupport();
//        syncWaitNotify();
//        lockAwaitSignal();
    }


    /**
     * 1. 正常使用 无锁块限制
     * 2. 可以先调用unpark()方法 再调用park()方法
     * * * 先调用unpark()会将Permit许可设置为1 当调用park时会判断当前Permit是否为1 如果为1 则将Permit设置为0 程序会继续向下执行
     * 3. 总结
     * * * lockSupport的park和unpark必须是成双成对的
     */
    public static void lockSupport(){
        Thread t1 = new Thread(() -> {
            try {
                /*
                 - 这里设置先试用unpark唤醒 再使用park阻塞 测试是否可以正常唤醒
                 - 如果这里设置的睡眠时间比t2线程的睡眠时间长 将导致程序不会停止 因为LockSupport最大值为1 不会叠加 也就是说当执行两遍
                 unpark()后再执行两遍park()时 第二个park()将阻塞当前线程
                 */
                 TimeUnit.SECONDS.sleep(2);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName()+ "\t"+"---第一次被唤醒");
            LockSupport.park();
            System.out.println(Thread.currentThread().getName() + "\t" + "---被唤醒");
        }, "t1");
        t1.start();

        new Thread(() -> {
            //唤醒第一个park
            LockSupport.unpark(t1);
            try { TimeUnit.SECONDS.sleep(3); } catch (InterruptedException e) { e.printStackTrace(); }
            //唤醒第二个park
            LockSupport.unpark(t1);
            System.out.println(Thread.currentThread().getName()+"\t"+"---发出通知");
        },"t2").start();
    }


    static Condition condition = lock.newCondition();


    /**
     * 1. 正常使用  Condition condition = lock.newCondition();
     * * * condition.await();   condition.signal();
     * 2. 异常情况
     * * * 不实用lock锁包裹: 去掉lock() unlock() 发现会抛出异常
     * * * * Exception in thread "t2" java.lang.IllegalMonitorStateException
     * * * * Exception in thread "t1" java.lang.IllegalMonitorStateException
     * 3. 测试signal()方法在await()之前执行
     * * * 发现程序不会终止
     * 4. 结论
     * * * lock、unlock对里面才能正确调用调用condition中线程等待和唤醒的方法
     * * * 先 await() 后 signal 才 OK ，否则线程无法被唤醒
     * * * 所以综上 await()/signal()方法 和 wait()/notify()方法一样 不能颠倒顺序执行
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
                //等待线程
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