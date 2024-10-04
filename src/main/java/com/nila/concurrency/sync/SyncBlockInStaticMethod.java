package com.nila.concurrency.sync;

class SendMail {
    static int count;

    static public void increment() {
        synchronized (SendEmail.class) {
            for (int i = 0; i < 100; i++) {
                System.out.println(Thread.currentThread().getName()+" increment: "+i);
                count++;
            }
        }
    }

    synchronized public void decrement() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName()+" decrement: "+i);
            count--;
        }
    }
}


public class SyncBlockInStaticMethod {
    public static void main(String[] args) {

        SendMail sendEmail = new SendMail();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                SendMail.increment();
            }
        });

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // both can work independently since 1 put lock on class other put lock on object
                sendEmail.decrement();
            }
        });
        thread1.start();
        thread2.start();
    }
}
