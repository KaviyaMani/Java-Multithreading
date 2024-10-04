package com.nila.concurrency.sync;

import java.util.concurrent.ArrayBlockingQueue;

class SendEmail {
    boolean status = false;

    public void send() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + " Sending Email "+i);

        }
        status = true;
    }

    public void unSend() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + " UnSending Email "+i);
        }
        status = false;
    }

}
class SIBOCounter {
    int count = 0;
    int count1 = 1000;
    final SendEmail sendEmail;

    SIBOCounter(SendEmail sendEmail) {
        this.sendEmail = sendEmail;
    }

    public void increment() {
        for (int i = 0; i < 1000; i++) {
            count++;
//            System.out.println(Thread.currentThread().getName() + " increment: ");
        }
        synchronized (this.sendEmail) {
            this.sendEmail.send();
        }
    }

    public void decrement() {
        for (int i = 0; i < 100; i++) {
            count++;
//            System.out.println(Thread.currentThread().getName() + " decrement: ");
        }
        synchronized (this.sendEmail) {
            this.sendEmail.unSend();
        }
    }

    synchronized public void incrementSyncMethod() {
        for (int i = 0; i < 100; i++) {
            System.out.println(Thread.currentThread().getName() +" increment sync method: "+i);
            count++;
        }
    }

    public void incrementNonSync() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() +" increment non sync: "+i);
            count++;
        }
    }

    public void print() {
        System.out.println(Thread.currentThread().getName() + " Just printing non synchronized ");
    }

    synchronized public void printSynchronized() {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName() + " Just printing synchronized ");
        }
    }

}

public class SynchronizedInstanceBlockObject {

    public static void main(String[] args) {


        SendEmail sendEmail = new SendEmail();
        SIBOCounter counter = new SIBOCounter(sendEmail);
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // everything executes in order inside same thread
//                counter.print();
//                counter.printSynchronized();

                counter.increment();
//                counter.decrement();

//                counter.print();
//                counter.printSynchronized();
            }
        }, "Thread-1");

        // this can happen simultaneously. since this is a non synchronized method
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                // S3: T1 sync block, T2 sync method. happen parallel since lock is on send email
                // counter.incrementSyncMethod();

                // S2: T1 sync block, T2 normal method. happen parallel
                // counter.incrementNonSync();

                // S1: this sync block execute parallel with T1 sync block only email fun happen synchronously
//                 counter.decrement();
            }
        }, "Thread-2");

        SendEmail sendEmail1 = new SendEmail();
        SIBOCounter counter1 = new SIBOCounter(sendEmail1);
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                // T1 send, T3 send work parallel since lock is on diff object
//                counter1.increment();
            }
        }, "Thread-3");

        SIBOCounter counter2 = new SIBOCounter(sendEmail);
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                // T1 send, T4 send can't work parallel since lock object is same
                counter2.increment();
            }
        }, "Thread-4");

        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
            thread4.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(counter.count);
        System.out.println(counter.count1);
    }
}
