package com.nila.concurrency;

public class ThreadLocalExample {
    public static void main(String[] args) {
        ThreadLocal<String> threadLocal = new ThreadLocal<>();
        threadLocal.set("Main");
        Thread thread1 = new Thread(()->{
            threadLocal.set("Thread-0");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+" : "+threadLocal.get());
        });
        Thread thread2 = new Thread(()->{
            threadLocal.set("Thread-1");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+" : "+threadLocal.get());
        });
        Thread thread3 = new Thread(()->{
            threadLocal.set("Thread-2");
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println(Thread.currentThread().getName()+" : "+threadLocal.get());
        });
        thread1.start();
        thread2.start();
        thread3.start();
        System.out.println(threadLocal.get());
    }
}
