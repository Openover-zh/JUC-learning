package com.zhang.juc.cf;


import java.util.concurrent.*;

/**
 * @auther zzyy
 * @create 2021-03-02 11:56
 */
public class CompletableFutureDemo
{
    static ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 20, 5L, TimeUnit.SECONDS, new LinkedBlockingQueue<>(50), Executors.defaultThreadFactory(), new ThreadPoolExecutor.AbortPolicy());
    public static void main(String[] args)throws Exception
    {
        CompletableFuture<String> future = CompletableFuture.runAsync(() -> {
            System.out.println("hello world");
            try {
                TimeUnit.SECONDS.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        },executor).thenRun(()->{
            System.out.println("thenRun01----start()------");
            try {
                 TimeUnit.SECONDS.sleep(2);
             } catch (InterruptedException e) {
                 e.printStackTrace();
             }
//            int i = 1/0; // 这里如果抛出异常 会直接走到handle方法里面 不会经过thenApply方法
            System.out.println("thenRun01---end()-------");
        }).thenApply((a)->{
            System.out.println("thenApply:"+a);
            return 0;
        }).handle((v,e)->{
            // handle方法有返回值 v代表前面的返回值 e代表异常信息
            System.out.println("handle value:"+v);
            System.out.println("handle throw:"+e);
//            int i = 1/0; // 如果在handle或者whenComplete中再出现异常 则最后会打印出异常
            return "hello world";
        }).whenComplete((v,e)->{
            // whenComplete和handle方法类似 但是没有返回值
            System.out.println("complete value:"+v);
            try {
                 TimeUnit.SECONDS.sleep(2);
             } catch (InterruptedException ex) {
                 ex.printStackTrace();
             }
            System.out.println("complete throw:"+e); // 这里的异常e 已经被上面handle捕获 所以这里e为null
        });
        // 如果调用get()是 future执行还没有结束 则抛出RuntimeException("exception-----") 运行时异常
//        future.completeExceptionally(new RuntimeException("exception----"));
        // 如果调用get()时 future还没有执行结束 则默认返回值 "0"
//        future.complete("0");
        System.out.println("get()");
        System.out.println(future.join()); //阻塞当前线程
        System.out.println("end()");
        executor.shutdown();
    }
}
