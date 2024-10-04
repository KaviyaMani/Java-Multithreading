package com.nila.concurrency.lock.stamped;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.concurrent.locks.StampedLock;

class SharedResourceS {
    boolean available = false;
    StampedLock lock = new StampedLock();

    public void produce() {
        long stamp = 0;
        try {
            stamp = lock.writeLock();
            System.out.println("Write lock acquired " + Thread.currentThread().getName());
            available = true;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Write lock released " + Thread.currentThread().getName());
            lock.unlockWrite(stamp);
        }
    }

    public void consume() {
        long stamp = 0;
        try {
            stamp = lock.readLock();
            System.out.println("Read lock acquired " + Thread.currentThread().getName());
            // System.out.println(available);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Read lock released " + Thread.currentThread().getName());
            lock.unlockRead(stamp);
        }
    }
}
public class StampedLockExample {
    public static void main(String[] args) {
        SharedResourceS shared = new SharedResourceS();
        Thread thread1 = new Thread(shared::produce, "Thread-1");
        // write lock has to wait for 1st lock to release
        Thread thread2 = new Thread(shared::produce, "Thread-2");
        Thread thread3 = new Thread(shared::consume, "Thread-3");
        // read locks can happen at a time
        Thread thread4 = new Thread(shared::consume, "Thread-4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
