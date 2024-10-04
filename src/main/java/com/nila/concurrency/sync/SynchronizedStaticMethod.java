package com.nila.concurrency.sync;

class StaticCounter {
    static int count = 0;
    static int count1 = 1000;

    synchronized static public void increment() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() +" increment: "+i);
            count++;
        }
    }

    synchronized static public void decrement() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() +" decrement: "+i);
            count1--;
        }
    }

}

public class SynchronizedStaticMethod {

    public static void main(String[] args) {
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                StaticCounter.increment();
                // Thread 1 acquire lock, complete increment. start decrement
                StaticCounter.decrement();
            }
        }, "Thread-1");

        // Since the lock is on class level, thread 2 wait for Thread 1 to release the lock on Class
        // After Thread 1 increment and decrement executed, Thread 2 starts. Since lock is on Class level
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                StaticCounter.increment();
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(StaticCounter.count);
    }
}
