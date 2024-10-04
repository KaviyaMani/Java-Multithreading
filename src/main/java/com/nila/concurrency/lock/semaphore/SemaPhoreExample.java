package com.nila.concurrency.lock.semaphore;

import java.util.concurrent.Semaphore;

class SharedResourceSemaPhore {
    boolean available = false;
    Semaphore lock = new Semaphore(2);
    public void getAccess() {
        try {
            lock.acquire();
            System.out.println("Lock acquired by: "+Thread.currentThread().getName());
            available = true;
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        } finally {
            lock.release();
            System.out.println("Lock release by: "+Thread.currentThread().getName());
        }
    }
}
public class SemaPhoreExample {
    public static void main(String[] args) {
        SharedResourceSemaPhore sharedResource = new SharedResourceSemaPhore();
        Thread thread1 = new Thread(sharedResource::getAccess, "Thread-1");
        // This can acquire lock along with T1, since semaphore permits 2 at a time
        Thread thread2 = new Thread(sharedResource::getAccess, "Thread-2");
        // This should wait for the lock to be released
        Thread thread3 = new Thread(sharedResource::getAccess, "Thread-3");
        // This also should wait for the lock to be released
        Thread thread4 = new Thread(sharedResource::getAccess, "Thread-4");
        thread1.start();
        thread2.start();
        thread3.start();
        thread4.start();

    }
}
