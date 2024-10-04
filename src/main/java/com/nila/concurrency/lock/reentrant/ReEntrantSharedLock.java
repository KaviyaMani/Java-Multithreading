package com.nila.concurrency.lock.reentrant;

import java.util.concurrent.locks.ReentrantLock;

class SharedResourceLocked {
    int count = 0;

    public void increment(ReentrantLock lock) {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" acquired lock");
            count++;
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println(Thread.currentThread().getName()+" released lock");
            lock.unlock();
        }
    }
}

public class ReEntrantSharedLock {
    public static void main(String[] args) {
        ReentrantLock lock = new ReentrantLock();
        SharedResourceLocked locked = new SharedResourceLocked();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                locked.increment(lock);
            }
        });

        // even if the objects are diff, locks are same hence only 1 thread can access the resource
        SharedResourceLocked locked1 = new SharedResourceLocked();
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                locked1.increment(lock);
            }
        });
        thread1.start();
        thread2.start();
    }
}
