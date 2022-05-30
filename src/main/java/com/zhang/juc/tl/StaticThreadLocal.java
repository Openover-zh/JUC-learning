package com.zhang.juc.tl;

import lombok.Data;
import org.springframework.scheduling.concurrent.ThreadPoolExecutorFactoryBean;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.function.Supplier;

class StaticThreadLocal {
    public static void main(String[] args) {
        new Thread(()->{
            User.threadLocal.get().setAge("11");
            System.out.println(User.threadLocal.get());
        },"t1").start();
        new Thread(()->{
            User.threadLocal.get().setName("222");
            System.out.println(User.threadLocal.get());
        },"t2").start();
    }


}
@Data
class User{
    private String name;
    private String age;

    static ThreadLocal<User> threadLocal = ThreadLocal.withInitial(User::new);


}
