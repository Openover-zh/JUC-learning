package com.zhang.juc.cf;


import java.util.concurrent.*;

/**
 * FutureTask
 * - 当调用get()方法时 不管是否计算完成都会导致阻塞
 * - 当
 * - isDone()轮询
 * - - 轮询的方式会耗费无谓的CPU资源，而且也不见得能及时地得到计算结果.
 * - - 如果想要异步获取结果,通常都会以轮询的方式去获取结果 尽量不要阻塞
 */
public class FutureTaskDemo
{
    public static void main(String[] args) throws ExecutionException, InterruptedException, TimeoutException
    {
        FutureTask<Integer> futureTask = new FutureTask<>(() -> {
            System.out.println(Thread.currentThread().getName() + "\t" + "---come in");
            try { TimeUnit.SECONDS.sleep(5); } catch (InterruptedException e) { e.printStackTrace(); }
            return 1024;
        });



        new Thread(futureTask,"t1").start();

        System.out.println(futureTask.get());//不见不散，只要出现get方法，不管是否计算完成都阻塞等待结果出来再运行


//        System.out.println(futureTask.get(2L,TimeUnit.SECONDS));//过时不候 超过指定时间报超时错误

        //不要阻塞，尽量用轮询替代
        while(true)
        {
            if(futureTask.isDone())
            {
                System.out.println("----result: "+futureTask.get());
                break;
            }else{
                System.out.println("还在计算中，别催，越催越慢，再催熄火");
            }
        }
    }

}
