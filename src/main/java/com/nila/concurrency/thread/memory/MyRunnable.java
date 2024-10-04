package com.nila.concurrency.thread.memory;

public class MyRunnable implements Runnable{

    private volatile int count = 0;

    @Override
    public void run() {
        for (int i = 0; i < 1000000; i++) {
            synchronized (this) {
                this.count++;
            }

//            System.out.println(Thread.currentThread().getName()+" : "+this.count);
        }
        System.out.println(Thread.currentThread().getName()+" : outside:  "+this.count);
    }
}
