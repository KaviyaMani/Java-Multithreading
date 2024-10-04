package com.nila.concurrency.problems;

class SharedResource {
    boolean itemAvailable = true;

    public synchronized void addItem() {
        System.out.println(Thread.currentThread().getName()+" adding item");
        this.itemAvailable = true;
        // If we didn't wake up the waiting thread, consumeItem thread will keep on waiting
        notifyAll();
    }

    public synchronized void consumeItem() {
        while (!itemAvailable) {
            try {
                System.out.println(Thread.currentThread().getName()+" waiting for item");
                wait(); // release all monitor lock, if it didn't release producer can't get the lock and produce
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println(Thread.currentThread().getName()+" consuming item");
        this.itemAvailable=false;
    }
}
public class ProducerConsumerWithBoolean {
    public static void main(String[] args) {
        SharedResource sharedResource = new SharedResource();

        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                sharedResource.consumeItem();
            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                try {
                    Thread.sleep(1000);
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
