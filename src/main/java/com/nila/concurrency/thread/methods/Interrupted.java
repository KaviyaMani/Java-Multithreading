package com.nila.concurrency.thread.methods;

public class Interrupted extends Thread{

    @Override
    public void run() {
        try {
        for (int i = 0; i < 5; i++) {
            System.out.println("Inside thread run: "+i);
                sleep(3000);
        }
        }catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) throws InterruptedException {
        Interrupted interrupted = new Interrupted();
        interrupted.start();
        while (!interrupted.isInterrupted()) {
            System.out.println("Thread not interrupted");
            sleep(2000);    // main thread sleep for 1 s
            interrupted.interrupt();
        }
        System.out.println("Thread interrupted");
    }
}
