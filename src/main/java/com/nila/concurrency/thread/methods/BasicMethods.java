package com.nila.concurrency.thread.methods;

import static java.lang.Thread.sleep;

public class BasicMethods {

    static {
        System.out.println("Just to check 0");
    }

    static {
        System.out.println("Just to check 1");
    }
    static {
        System.out.println("Just to check 2");
    }
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Current thread running: " + Thread.currentThread());
        System.out.println("Thread State: " + Thread.currentThread().getState());
        System.out.println("Thread Group: "+Thread.currentThread().getThreadGroup());
        System.out.println("Priority: "+Thread.currentThread().getPriority());
        System.out.println("Name: "+Thread.currentThread().getName());
        Thread.currentThread().setName("Test");
        System.out.println("New Name: "+Thread.currentThread().getName());
        System.out.println("Id: "+Thread.currentThread().getId());
        System.out.println("Alive: "+Thread.currentThread().isAlive());
        System.out.println("Daemon: "+Thread.currentThread().isDaemon());
        System.out.println("Interrupted: "+Thread.currentThread().isInterrupted());
        System.out.println("Active thread count  " + Thread.activeCount());

        for (int i = 0; i < 5; i++) {
            System.out.println("Value: "+i);
            sleep(1000);
        }

        Thread newThread = new Thread(()->{
            for (int i = 0; i < 5; i++) {
                System.out.println("Thread: "+Thread.currentThread().getName()+" "+i);
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        newThread.start();
    }
    static {
        System.out.println("Just to check 4");
    }
}
