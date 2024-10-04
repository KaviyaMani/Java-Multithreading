package com.nila.concurrency.completablefuture;

import java.util.concurrent.*;

public class CompletableFutureThenApply {
    public static void main(String[] args) throws ExecutionException, InterruptedException {

        ThreadPoolExecutor executor = new ThreadPoolExecutor(2, 2, 3000, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(2));

        // passing custom executor
        CompletableFuture<String> task1 = CompletableFuture.supplyAsync(() -> {
            return "Hello";
        }, executor);
        try {
            String s = task1.get();
            System.out.println("Got the result: "+s);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }

        CompletableFuture<String> thenApply = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+" in supplyAsync");
            return "supplyAsync + ";
        }, executor).thenApply(str -> {
            System.out.println(Thread.currentThread().getName()+" in thenApply");
            return str + "thenApply";
        });
        System.out.println(thenApply.get());

        // Can chain with the result. no need to do it in same execution
        CompletableFuture<String> thenApplySeparate = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+" in supplyAsync");
            return "supplyAsync + ";
        }, executor);

        CompletableFuture<String> thenApplySeparateResult = thenApplySeparate.thenApply(str -> {
            System.out.println(Thread.currentThread().getName()+" in thenApply");
            return str + "thenApply";
        });
        System.out.println(thenApplySeparateResult.get());

        // same executor
        CompletableFuture<String> thenApplyAsync = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+" in supplyAsync");
            return "supplyAsync + ";
        }, executor).thenApplyAsync(str -> {
            System.out.println(Thread.currentThread().getName()+" in thenApplyAsync");
            return str + "thenApplyAsync";
        }, executor);
        System.out.println(thenApplyAsync.get());

        // different executor, thenApplyAsync thread will be from Fork Join Pool
        CompletableFuture<String> thenApplyAsyncDefaultExecutor = CompletableFuture.supplyAsync(() -> {
            System.out.println(Thread.currentThread().getName()+" in supplyAsync");
            return "supplyAsync + ";
        }, executor).thenApplyAsync(str -> {
            System.out.println(Thread.currentThread().getName()+" in thenApplyAsync");
            return str + "thenApplyAsync in different thread";
        });
        System.out.println(thenApplyAsyncDefaultExecutor.get());


    }
}
