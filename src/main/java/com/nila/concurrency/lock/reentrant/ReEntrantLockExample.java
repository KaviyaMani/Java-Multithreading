package com.nila.concurrency.lock.reentrant;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

class SharedResource {
    int count = 0;
    ReentrantLock lock = new ReentrantLock();

    public void increment() {
        System.out.println(Thread.currentThread().getName()+" before lock ");
        lock.lock();
        for (int i = 0; i < 10; i++) {
            System.out.println(Thread.currentThread().getName()+" "+i);
        }
        lock.unlock();
        System.out.println(Thread.currentThread().getName()+" after lock ");
    }
}

class CounterLock {

    long count = 0;

    private Lock lock = new ReentrantLock();

    public void increment() {
        try {
            lock.lock();
            this.count++;
        } finally {
            // If operation failed for any reason lock should be unlocked
            lock.unlock();
        }
    }

    public void decrement() {
        try {
            lock.lock();
            this.count--;
        } finally {
            lock.unlock();
        }
    }

    public void perform(String op ) {
        lock.lock();
        switch (op) {
            case "ADD": increment();
            case "SUBTRACT": decrement();
        }
        lock.unlock();
    }
}

class CounterSynchronized {

    private long count = 0;

    public synchronized void inc() {
        this.count++;
    }

    public synchronized long getCount() {
        return this.count;
    }
}
public class ReEntrantLockExample {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                sharedResource.increment();
            }
        }, "Thread-1");
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // T2 can print before block parallel but have to wait to execute resource b/w lock
                sharedResource.increment();
            }
        }, "Thread-2");
        thread1.start();
        thread2.start();

        CounterLock counterLock = new CounterLock();
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    counterLock.perform("ADD");
                }
            }
        }, "Thread-3");
        // In this perform method is having lock, add & subtract is also having lock, acquiring lock inside lock is possible only by having ReentrantLock
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 10; i++) {
                    counterLock.perform("SUBTRACT");
                }
            }
        }, "Thread-4");
        thread3.start();
        thread4.start();

        try {
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Count: "+counterLock.count);
    }
}
