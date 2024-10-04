package com.nila.concurrency.lock.readwrite;

import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

class SharedResourceRW {
    boolean available = false;

    public void produce(ReadWriteLock lock) {
        try {
            lock.writeLock().lock();
            System.out.println("Write lock acquired "+Thread.currentThread().getName());
            available = true;
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Write lock released "+Thread.currentThread().getName());
            lock.writeLock().unlock();
        }
    }

    public void consume(ReadWriteLock lock) {
        try {
            lock.readLock().lock();
            System.out.println("Read lock acquired "+Thread.currentThread().getName());
            // System.out.println(available);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Read lock released "+Thread.currentThread().getName());
            lock.readLock().unlock();
        }
    }
}
public class ReadWriteLockExample {
    public static void main(String[] args) {
        ReadWriteLock lock = new ReentrantReadWriteLock();
        SharedResourceRW shared = new SharedResourceRW();
        Thread thread1 = new Thread(() -> {
            shared.produce(lock);
        }, "Thread-1");
        // write lock has to wait for 1st lock to release
        Thread thread2 = new Thread(() -> {
            shared.produce(lock);
        }, "Thread-2");
        Thread thread3 = new Thread(() -> {
            shared.consume(lock);
        }, "Thread-3");
        // read locks can happen at a time
        Thread thread4 = new Thread(() -> {
            shared.consume(lock);
        }, "Thread-4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
