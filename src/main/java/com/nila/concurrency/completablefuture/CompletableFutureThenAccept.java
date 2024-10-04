package com.nila.concurrency.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureThenAccept {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2));

        // multiple supplyAsync execute in order no matter what
        CompletableFuture<Void> task1 = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println(Thread.currentThread().getName()+ " - task1");
                    return "Hello";
                }, executor)
                .thenAccept((String val) -> {
                    System.out.println("All stages completed. Final value: " + val);
                    System.out.println("Final thread: "+Thread.currentThread().getName());
                });

        CompletableFuture<Void> task2 = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println(Thread.currentThread().getName()+ " - First");
                    return "First";
                }, executor)
                .thenCompose((String val) -> {
                    return CompletableFuture.supplyAsync(() -> {
                        System.out.println(Thread.currentThread().getName()+ " - Second");
                        return val + " Second";
                    });
                })
                .thenComposeAsync((String val) -> {
                    return CompletableFuture.supplyAsync(() -> {
                        System.out.println(Thread.currentThread().getName()+ " - Third");
                        return val + " Third";
                    });
                })
                .thenAcceptAsync((String val) -> {
                    System.out.println("Final result: " + val);
                    System.out.println("Final thread Async: "+Thread.currentThread().getName());
                });
    }
}
