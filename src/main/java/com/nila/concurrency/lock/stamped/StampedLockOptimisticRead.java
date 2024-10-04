package com.nila.concurrency.lock.stamped;

import java.util.concurrent.locks.StampedLock;

class SharedResourceOp {
    int value = 5;
    StampedLock lock = new StampedLock();

    public void produce() {
        long stamp = 0;
        try {
            stamp = lock.writeLock();
            System.out.println("Write lock acquired " + Thread.currentThread().getName());
            value = 6;
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            System.out.println("Write lock released " + Thread.currentThread().getName());
            lock.unlockWrite(stamp);
        }
    }

    public void dirtyConsume() {
        try {
            long stamp = lock.tryOptimisticRead();
            value = 6;
            if (lock.validate(stamp)) {
                System.out.println("Value did not updated can use "+Thread.currentThread().getName());
            } else {
                System.out.println("Value got updated in between "+Thread.currentThread().getName());
                value = 5; //rollback
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
    public void pristineConsume() {
        try {
            Thread.sleep(2000);
            value = 7;
            long stamp = lock.tryOptimisticRead();
            if (lock.validate(stamp)) {
                System.out.println("Value did not updated can use "+Thread.currentThread().getName());
            } else {
                System.out.println("Value got updated in between "+Thread.currentThread().getName());
                value = 6; // rollback if write happened in between
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
public class StampedLockOptimisticRead {
    public static void main(String[] args) {
        SharedResourceOp shared = new SharedResourceOp();
        Thread thread1 = new Thread(shared::produce, "Thread-1");
        // write lock has to wait for 1st lock to release
        Thread thread2 = new Thread(shared::produce, "Thread-2");
        // Since this happen parallel with other write value got changed in between cannot proceed
        Thread thread3 = new Thread(shared::dirtyConsume, "Thread-3");
        // Since this happens after 2s all the updates already happened before getting the lock, hence we can use the value
        Thread thread4 = new Thread(shared::pristineConsume, "Thread-4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
    }
}
