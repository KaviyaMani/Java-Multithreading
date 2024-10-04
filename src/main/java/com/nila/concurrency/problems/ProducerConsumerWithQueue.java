package com.nila.concurrency.problems;

import java.util.LinkedList;
import java.util.Queue;

class SharedResourceQ {
    Queue<Integer> queue;
    int size;

    SharedResourceQ(int size) {
        queue = new LinkedList<>();
        this.size = size;
    }

    public synchronized void addItem(int item) {
        while(queue.size() == size) {
            try {
                System.out.println(" Producer is waiting for consumer to consume");
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Produced : "+item);
        queue.add(item);
        // If we didn't wake up the waiting thread, consumeItem thread will keep on waiting
        notify();
    }

    public synchronized void consumeItem(int item) {
        while (queue.isEmpty()) {
            try {
                System.out.println("Consumer is waiting for producer to produce");
                wait(); // release all monitor lock, if it didn't release producer can't get the lock and produce
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        queue.poll();
        System.out.println("Consumed : "+item);
        notify();
    }
}
public class ProducerConsumerWithQueue {
    public static void main(String[] args) {
        SharedResourceQ sharedResource = new SharedResourceQ(3);

        Thread thread1 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(10);
                    sharedResource.consumeItem(i);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }

            }
        }, "Thread-1");

        Thread thread2 = new Thread(() -> {
            for (int i = 1; i <= 10; i++) {
                try {
                    Thread.sleep(100);
                    sharedResource.addItem(i);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        }, "Thread-2");

        thread1.start();
        thread2.start();
    }

}
