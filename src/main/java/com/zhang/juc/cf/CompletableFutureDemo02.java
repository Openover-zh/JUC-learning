package com.zhang.juc.cf;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;

public class CompletableFutureDemo02 {
    public static void main(String[]args) throws ExecutionException, InterruptedException {
        CompletableFuture.supplyAsync(() -> {
            return 1;
        }).thenApply(f -> {
            return f + 2;
        }).thenApply(f -> {
            return f + 3;
        }).thenApply(f -> {
            return f + 4;
        }).thenAccept(r -> System.out.println(r));
    }
}
