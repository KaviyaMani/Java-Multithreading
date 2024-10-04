package com.nila.concurrency.lock.condition;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

class SharedResourceC {
    boolean itemAvailable = true;
    ReentrantLock lock = new ReentrantLock();
    Condition condition = lock.newCondition();
    public void addItem() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" acquired lock");
            while (itemAvailable) {
                System.out.println(Thread.currentThread().getName()+" waiting for consumer to consume item");
                condition.await();
            }
            System.out.println(Thread.currentThread().getName()+" adding item");
            this.itemAvailable = true;
            condition.signal();
        } catch (Exception e) {
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+" released lock");
        }
    }

    public void consumeItem() {
        try {
            lock.lock();
            System.out.println(Thread.currentThread().getName()+" acquired lock");
            while (!itemAvailable) {
                System.out.println(Thread.currentThread().getName()+" waiting for producer to produce item");
                condition.await();
            }
            System.out.println(Thread.currentThread().getName()+" consuming item");
            this.itemAvailable=false;
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.unlock();
            System.out.println(Thread.currentThread().getName()+" released lock");
        }
    }
}
public class ConditionInLockExample {
    public static void main(String[] args) {
        SharedResourceC sharedResource = new SharedResourceC();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                sharedResource.consumeItem();
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                sharedResource.addItem();
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();
    }

}
