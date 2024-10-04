package com.nila.concurrency.future;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;

public class SimpleFutureCallable {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        ThreadPoolExecutor pool = new ThreadPoolExecutor(1, 1, 2000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(3), new ThreadPoolExecutor.DiscardPolicy());

        Callable<Integer> callable1 = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return 25;
            }
        };
        Future<?> task1 = pool.submit(callable1);
        Object task1Result = task1.get();
        System.out.println("Task 1 result: "+task1Result);

        Callable<List<Integer>> callable2 = new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                return Arrays.asList(1,10,23,45,67,87);
            }
        };
        Future<?> task2 = pool.submit(callable2);
        Object task2Result = task2.get();
        System.out.println("Task 2 result: "+task2Result);
    }
}
