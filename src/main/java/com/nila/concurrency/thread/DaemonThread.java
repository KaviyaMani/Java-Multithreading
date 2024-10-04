package com.nila.concurrency.thread;

public class DaemonThread {

    public static void main(String[] args) throws InterruptedException {

        Thread thread = new Thread(() -> {
            System.out.println("Started executing Daemon thread");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            // This won't be printed since main thread will finish executing before hence Daemon thread stopped abruptly
            System.out.println("Daemon thread finished executing");
        });
        thread.setDaemon(true);
        thread.start();
        Thread.sleep(1000);
        System.out.println("Main thread finished executing");
    }
}
