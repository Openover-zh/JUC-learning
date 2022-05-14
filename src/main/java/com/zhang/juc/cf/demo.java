package com.zhang.juc.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

public class demo {
    public static void main(String[] args) throws Exception {
        /*CompletableFuture<Integer> completableFuture = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName() + " \t " + "-----come in");
            int result = ThreadLocalRandom.current().nextInt(10); // 暂停几秒钟线程
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("----- 计算结束耗时 1 秒钟， result ： " + result);
            if (result > 6) {
                int age = 10 / 0;
            }
            return result;
        }).whenComplete((v, e) -> {
            if (e == null) {
                System.out.println("-----result: " + v);
            }
        }).exceptionally(e -> {
            System.out.println("-----exception: " + e.getCause() + " \t " + e.getMessage());
            return -44;
        });
        // 主线程不要立刻结束，否则 CompletableFuture 默认使用的线程池会立刻关闭 : 暂停 3 秒钟线程
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        System.out.println(CompletableFuture.runAsync(() -> {
            int i = 1 / 0;
        }).get());

    }
}
