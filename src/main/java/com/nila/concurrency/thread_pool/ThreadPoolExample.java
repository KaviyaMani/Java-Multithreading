package com.nila.concurrency.thread_pool;

import java.util.concurrent.*;

public class ThreadPoolExample {

    public static void main(String[] args) throws InterruptedException {

        // can use Executors.defaultThreadFactory()
        CustomThreadFactory threadFactory = new CustomThreadFactory();

        // can use new ThreadPoolExecutor.DiscardPolicy()
        CustomRejectionHandler rejectionHandler = new CustomRejectionHandler();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(2, 4, 5000, TimeUnit.MILLISECONDS,
                new ArrayBlockingQueue<>(2), threadFactory, rejectionHandler);

        // only by setting this keepAlive will work
        threadPool.allowCoreThreadTimeOut(true);
        for (int i = 0; i < 10; i++) {
            threadPool.submit(()-> {
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                System.out.println("Task executed by thread: "+Thread.currentThread().getName());
            });
        }
        Thread.sleep(4000);
        // 2 threads take 2 tasks, 2 tasks goes to queue, 2 threads created again 2 tasks goes there
        // remaining 4 tasks got rejected
        System.out.println("Active count: "+threadPool.getActiveCount());
        System.out.println("Completed Task Count: "+threadPool.getCompletedTaskCount());
        System.out.println("Task count: "+threadPool.getTaskCount());
    }
}

class CustomThreadFactory implements ThreadFactory {

    @Override
    public Thread newThread(Runnable r) {
        Thread thread = new Thread(r);
        thread.setPriority(Thread.NORM_PRIORITY);
        thread.setDaemon(false);
        // thread.setName("th"+increment); need poolnumber and increment logic for unique thread name
        return thread;
    }
}

class CustomRejectionHandler implements RejectedExecutionHandler {

    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.println("Task rejected: "+r.toString());
    }
}