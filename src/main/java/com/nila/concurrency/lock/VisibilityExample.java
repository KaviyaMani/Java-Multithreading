package com.nila.concurrency.lock;

class VCounter {
    int count = 0;

    public void increment() {
        count++;
    }
    public int getCount() {
        return this.count;
    }

    synchronized public void incrementSync() {
        count++;
    }
}
public class VisibilityExample {

    public static void main(String[] args) {
        VCounter counter = new VCounter();
        // By calling non-sync counter each variable update is not flushed to main memory, cannot predict output
        // By calling sync counter, values are flushed to main memory, atleast 2nd thread achieves expected result
        Thread thread1 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    // counter.increment();
                    counter.incrementSync();
                }
                System.out.println(counter.getCount());
            }
        },"Thread-1");

        Thread thread2 = new Thread(new Runnable() {
            @Override
            public void run() {
                for (int i = 0; i < 100000; i++) {
                    // counter.increment();
                    counter.incrementSync();
                }
                System.out.println(counter.getCount());
            }
        },"Thread-2");


        thread1.start();
        thread2.start();
    }

}
