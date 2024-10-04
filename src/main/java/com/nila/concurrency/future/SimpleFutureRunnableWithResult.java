package com.nila.concurrency.future;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

class SharedUpdate implements Runnable {

    List<Integer> values;
    AtomicInteger count = new AtomicInteger(0);

    SharedUpdate(List<Integer> val) {
        values = val;
    }

    @Override
    public void run() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        this.values.add(count.incrementAndGet());
    }

}
public class SimpleFutureRunnableWithResult {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        List<Integer> values = new ArrayList<>();
        SharedUpdate sharedUpdate = new SharedUpdate(values);
        ThreadPoolExecutor executor = new ThreadPoolExecutor(1, 1, 100, TimeUnit.MILLISECONDS, new ArrayBlockingQueue<>(4),
                Executors.defaultThreadFactory(), new ThreadPoolExecutor.DiscardPolicy());
        Future<List<Integer>> task1 = executor.submit(sharedUpdate, values);
        Future<List<Integer>> task2 = executor.submit(sharedUpdate, values);

        // [1]
        System.out.println(task1.get());
        // [1, 2]
        System.out.println(task2.get());
    }
}
