package com.nila.concurrency.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureThenCompose {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2));

        // multiple supplyAsync execute in order no matter what
        CompletableFuture<String> task1 = CompletableFuture
                .supplyAsync(() -> {
                    System.out.println(Thread.currentThread().getName()+ " - First");
                    return "Hello";
                }, executor)
                .thenCompose((String val) -> {
                    return CompletableFuture.supplyAsync(() -> {
                        System.out.println(Thread.currentThread().getName()+ " - Second");
                        return val + " Hi";
                    });
                })
                .thenComposeAsync((String val) -> {
                    return CompletableFuture.supplyAsync(() -> {
                        System.out.println(Thread.currentThread().getName()+ " - Third");
                        return val + " last";
                    });
                });
        try {
            String s = task1.get();
            System.out.println("Got the result: " + s);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
