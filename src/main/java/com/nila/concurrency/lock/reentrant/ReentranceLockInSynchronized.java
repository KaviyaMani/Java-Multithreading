package com.nila.concurrency.lock.reentrant;

class Counter {
    int count = 0;

    synchronized public void increment() {
        System.out.println(Thread.currentThread().getName()+" increment");
        count++;
        incrementBy2();
    }

    synchronized public void incrementBy2() {
        System.out.println(Thread.currentThread().getName()+" incrementBy2");
        count += 2;
    }
}

public class ReentranceLockInSynchronized {
    public static void main(String[] args) {
        Counter counter = new Counter();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                counter.increment();
            }
        }, "Thread-1");
        thread1.start();
    }

}
