package com.nila.concurrency.thread;

public class ImplementsRunnable implements Runnable{
    @Override
    public void run() {
        System.out.println("Inside Runnable: " + Thread.currentThread().getName());
    }
}
