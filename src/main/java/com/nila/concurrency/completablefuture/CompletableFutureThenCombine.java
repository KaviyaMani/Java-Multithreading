package com.nila.concurrency.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureThenCombine {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2));

        // multiple supplyAsync execute in order no matter what
        CompletableFuture<String> task1 = CompletableFuture
                .supplyAsync(() -> {
                    return "Hello ";
                }, executor);

        CompletableFuture<String> task2 = CompletableFuture
                .supplyAsync(() -> {
                    return "First ";
                }, executor)
                .thenCompose((String val) -> {
                    return CompletableFuture.supplyAsync(() -> {
                        return val + "Second ";
                    });
                });
        CompletableFuture<String> finalResult = task1.thenCombine(task2, (String s1, String s2)->s1+s2);
        System.out.println("Final combined result before completion: "+finalResult);

        CompletableFuture.allOf(task1, task2).join();

        CompletableFuture<String> finalResult1= task1.thenCombine(task2, (String s1, String s2)->s1+s2);
        System.out.println("Final combined result: "+finalResult1.get());
    }
}
