package com.nila.concurrency.thread_pool;

import java.util.concurrent.*;

public class ExecutorServiceExample {

    public static void main(String[] args) {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        // newFixedThreadPool - internally uses ThreadPoolExecutor, if we want to customize more we can directly use this
        ExecutorService executor1 = new ThreadPoolExecutor(
                    5,10, 3000, TimeUnit.MILLISECONDS,
                    new ArrayBlockingQueue<>(128));
        for (int i = 0; i < 10; i++) {
            executor.submit(taskWrapper("Task "+i));
        }
        // If we did not shut down, program won't exit will wait for other inputs
        executor.shutdown();
    }

    private static Runnable taskWrapper(String message) {
        return new Runnable() {
            @Override
            public void run() {
                System.out.println(Thread.currentThread().getName()+" "+message);
            }
        };
    }
}
