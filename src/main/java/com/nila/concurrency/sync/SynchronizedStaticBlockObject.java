package com.nila.concurrency.sync;

class SendEmailS {
    boolean status = false;

    public void send() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + " Sending Email");
        }
        status = true;
    }

    public void unSend() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName() + " UnSending Email");
        }
        status = false;
    }

}
class SSBOCounter {
    int count = 0;
    int count1 = 1000;
    final SendEmailS sendEmail;

    SSBOCounter(SendEmailS sendEmail) {
        this.sendEmail = sendEmail;
    }

    public void increment() {
        for (int i = 0; i < 1000; i++) {
            count++;
//            System.out.println(Thread.currentThread().getName() + " increment: ");
        }
        synchronized (SendEmailS.class) {
            this.sendEmail.send();
        }
    }

    public void decrement() {
        for (int i = 0; i < 100; i++) {
            count++;
            System.out.println(Thread.currentThread().getName() + " decrement: ");
        }
        synchronized (SendEmailS.class) {
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

public class SynchronizedStaticBlockObject {

    public static void main(String[] args) {


        SendEmailS sendEmail = new SendEmailS();
        SSBOCounter counter = new SSBOCounter(sendEmail);
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

                // S1: T1 static sync block, T2 static sync block happen parallel, sync block alone happen synchronously
                 // counter.decrement();
            }
        }, "Thread-2");

        SendEmailS sendEmail1 = new SendEmailS();
        SSBOCounter counter1 = new SSBOCounter(sendEmail1);
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                // T1 send, T3 send work synchronously even if it is on diff thread, since lock is on class
                counter1.increment();
            }
        }, "Thread-3");

        thread1.start();
        thread2.start();
        thread3.start();
        try {
            thread1.join();
            thread2.join();
            thread3.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(counter.count);
        System.out.println(counter.count1);
    }
}
