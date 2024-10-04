package com.nila.concurrency.atomic;

import java.util.concurrent.atomic.AtomicInteger;

class SharedAtomicResource {
    AtomicInteger value = new AtomicInteger(0);
    int val = 0;

    public void atomicIncrement() {
        value.incrementAndGet();
    }

    public void normalIncrement() {
        val++;
    }

    public void availableMethods() {
        value.addAndGet(1);
        value.getAndAdd(1);
        value.getAndIncrement();
        value.getAndDecrement();
        value.decrementAndGet();
        value.floatValue();
    }
}

public class AtomicVariable {

    public static void main(String[] args) {
        SharedAtomicResource resource = new SharedAtomicResource();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                resource.atomicIncrement();
            }
        }, "Thread-1");
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                resource.atomicIncrement();
            }
        }, "Thread-2");
        Thread thread3 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                resource.normalIncrement();
            }
        }, "Thread-3");
        Thread thread4 = new Thread(() -> {
            for (int i = 0; i < 100000; i++) {
                resource.normalIncrement();
            }
        }, "Thread-4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("Atomic increment value after 200000 increments: "+resource.value);
        System.out.println("Normal increment value after 200000 increments: "+resource.val);
    }
}
