package com.zhang.juc.rwlock;

import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

public class ReLockDemo {
    public static void main(String[] args) {
        // stampedLock不支持重入
        new Thread(()->{
            StampedLock stampedLock = new StampedLock();
            long l = stampedLock.tryWriteLock();
            System.out.println("-------"+l);
            long z = stampedLock.writeLock();
            System.out.println("------"+z);
            stampedLock.unlock(z);
        }).start();

        new Thread(()->{
            ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
            ReentrantReadWriteLock.WriteLock writeLock = lock.writeLock();
            writeLock.lock();
            System.out.println("111111");
            writeLock.lock();
            System.out.println("2222222222");
            writeLock.unlock();
            System.out.println("3333333");
        }).start();
    }
}
