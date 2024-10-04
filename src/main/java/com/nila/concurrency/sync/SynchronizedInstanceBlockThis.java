package com.nila.concurrency.sync;

class SIBCounter {
    int count = 0;
    int count1 = 1000;

    public void increment() {
        for (int i = 0; i < 1000; i++) {
            synchronized (this) {
                System.out.println(Thread.currentThread().getName() +" increment: "+i);
                count++;
            }
        }
    }

    public void decrement() {
        for (int i = 0; i < 1000; i++) {
            System.out.println(Thread.currentThread().getName()+" decrement: "+i);
            count1--;
        }
    }

    public void print() {
        System.out.println(Thread.currentThread().getName()+" Just printing non synchronized ");
    }

    public void printSynchronized() {
        synchronized (this) {
            System.out.println(Thread.currentThread().getName()+" Just printing synchronized ");
        }
    }

}

public class SynchronizedInstanceBlockThis {

    public static void main(String[] args) {

        // works same as SynchronizedInstanceMethod - Since we are using "this" as a monitor lock

        SIBCounter counter = new SIBCounter();
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                // everything executes in order inside same thread
                counter.print();
                counter.printSynchronized();

                // Synchronize task gets completed before starting next task. increment gets completed before calling decrement
                counter.increment();
                counter.decrement();

                counter.print();
                counter.printSynchronized();
            }
        }, "Thread-1");

        // this can happen simultaneously. since this is a non synchronized method
        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                counter.decrement();
            }
        }, "Thread-2");

        // this will happen only after thread1 execution is complete. since it is also calling the shared resource with the same instance.
        Thread thread3 = new Thread(new Runnable() {
            @Override
            public void run() {
                counter.increment();
            }
        }, "Thread-3");

        // This can happen simultaneously with Thread 1 and Thread 2 not like Thread 3. Since it is holding a lock on another instance
        SIBCounter counterNew = new SIBCounter();
        Thread thread4 = new Thread(new Runnable() {
            @Override
            public void run() {
                counterNew.increment();
            }
        }, "New Instance Thread ");
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
        System.out.println(counterNew.count);
    }
}
