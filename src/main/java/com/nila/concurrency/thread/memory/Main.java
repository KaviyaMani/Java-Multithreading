package com.nila.concurrency.thread.memory;

public class Main {

    public static void main(String[] args) {
        MyRunnable runnable1 = new MyRunnable();
        MyRunnable runnable2 = new MyRunnable();

        // Separate object - since each thread have separate object with separate count, there no issue with count variable
        Thread thread1 = new Thread(runnable1, "Thread-1");
        Thread thread2 = new Thread(runnable2, "Thread-2");

//        thread1.start();
//        thread2.start();

        // Shared object - since both thread use same count variable, visibility issue happens. one thread cannot properly see other threads update hence upredictable o/p
        Thread thread3 = new Thread(runnable1, "Thread-3");
        Thread thread4 = new Thread(runnable1, "Thread-4");

        thread3.start();
        thread4.start();

    }
}
