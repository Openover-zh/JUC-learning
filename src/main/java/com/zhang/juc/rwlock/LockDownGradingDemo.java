package com.zhang.juc.rwlock;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @auther zzyy
 * @create 2021-03-28 10:18
 * 锁降级：遵循获取写锁→再获取读锁→再释放写锁的次序，写锁能够降级成为读锁。
 *
 * 如果一个线程占有了写锁，在不释放写锁的情况下，它还能占有读锁，即写锁降级为读锁。
 */
public class LockDownGradingDemo
{
    public static void main(String[] args)
    {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

        ReentrantReadWriteLock.ReadLock readLock = readWriteLock.readLock();
        ReentrantReadWriteLock.WriteLock writeLock = readWriteLock.writeLock();

        //有且只有一个线程main，来验证锁降级策略要求
        // 读写锁是互斥的 获取写锁后不能继续获取读锁
        new Thread(()->{
            writeLock.lock();
            try {
                TimeUnit.SECONDS.sleep(3);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("------writer");
            writeLock.unlock();
        }).start();

        new Thread(()->{
            try {
                 TimeUnit.SECONDS.sleep(1);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
            readLock.lock();
            System.out.println("-----read");
            //暂停几秒钟线程
            try { TimeUnit.SECONDS.sleep(2); } catch (InterruptedException e) { e.printStackTrace(); }
            readLock.unlock();
        }).start();
//
//        writeLock.lock();
//        System.out.println("-----1111");
//        writeLock.unlock();
//
//
//
//        System.out.println("-----2222");
//
//        writeLock.lock();
//        try
//        {
//            System.out.println("------33333");
//            //biz  l;ajfd;lakjsfd;lksajd;lksajf;lakjfds;
//            //本次写完立刻被读取。
//            /*
//            * 1
//            * 2
//            * 3
//            * 4
//            * 5----biz end
//            * */
//            readLock.lock();
//            System.out.println("-------44444");
//        }catch (Exception e){
//            e.printStackTrace();
//        }finally {
//            writeLock.unlock();
//        }






    }
}
