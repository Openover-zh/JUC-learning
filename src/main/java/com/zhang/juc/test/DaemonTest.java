package com.zhang.juc.test;

import java.util.concurrent.TimeUnit;

/**
 * 如果设置Daemon为true(守护线程) 表示不用等待该线程执行完毕 主线程直接结束
 * 如果设置Daemon为false(用户线程) 表示需要等待该线程结束 主线程才会结束
 */
public class DaemonTest {
    public static void main(String[] args) {
        Thread a = new Thread(() -> {
            while (true) {
            }
        }, "a");
//        该值默认为false
        a.setDaemon(true);
        a.start();
        try {
             TimeUnit.SECONDS.sleep(2);
         } catch (InterruptedException e) {
             e.printStackTrace();
         }
        System.out.println("主线程运行完毕!!!");
    }
}
