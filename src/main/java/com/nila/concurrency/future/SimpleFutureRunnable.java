package com.nila.concurrency.future;

import java.util.concurrent.*;

public class SimpleFutureRunnable {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 2000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.DiscardPolicy());

        Future<?> task1 = pool.submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("Doing something");
            }
        });
        System.out.println("Is task1 completed: "+task1.isDone());
        System.out.println("Is task1 cancelled: "+task1.isCancelled());
        Future<?> task2 = pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        System.out.println("Is task1 completed: "+task1.isDone());
        System.out.println("Is task2 completed: "+task2.isDone());
        task2.cancel(true);
        System.out.println("Is task2 completed: "+task2.isDone());
        System.out.println("Is task2 cancelled: "+task2.isCancelled());

        Future<?> task3 = pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Object result = task3.get();
        System.out.println("Main thread waited for task3");
        System.out.println(result);

        Future<?> task4 = pool.submit(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        Object result1 = null;
        try {
            result1 = task4.get(1000, TimeUnit.MILLISECONDS);
        } catch (TimeoutException e) {
            // wait for 1s and throws exception
            throw new RuntimeException(e);
        }
        System.out.println("Main thread waited for task4");
        System.out.println(result1);

    }
}
