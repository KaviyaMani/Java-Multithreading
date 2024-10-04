package com.nila.concurrency.thread.memory;

public class MyRunnableSynchronized implements Runnable{

    private int count = 0;

    @Override
    public synchronized void run() {
        for (int i = 0; i < 10; i++) {
            this.count++;
            System.out.println(Thread.currentThread().getName()+" : "+this.count);
        }
        System.out.println(Thread.currentThread().getName()+" : outside:  "+this.count);
    }
}
