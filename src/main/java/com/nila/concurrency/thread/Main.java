package com.nila.concurrency.thread;

public class Main {

    public static void main(String[] args) {

        // Extend Thread class
        /*System.out.println("Inside : " + Thread.currentThread().getName());
        ExtendThread thread = new ExtendThread();
        thread.start();
        // won't create new thread uses main thread itself
        // thread.run();
        System.out.println("Finished executing main thread: "+ Thread.currentThread().getName());*/

        // Implements Runnable
        System.out.println("Inside : " + Thread.currentThread().getName());
        ImplementsRunnable thread = new ImplementsRunnable();
        Thread thread1 = new Thread(thread);
        System.out.println("Thread State before starting: " + thread1.getState());
        thread1.start();
        System.out.println("Thread State after starting: " + thread1.getState());
        System.out.println("Finished executing main thread: "+ Thread.currentThread().getName());

        //Implement using anonymous inner class
        /*System.out.println("Inside : " + Thread.currentThread().getName());
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                System.out.println("Inside Anonymous Runnable: " + Thread.currentThread().getName());
            }
        };
        Thread anonThread = new Thread(runnable);
        anonThread.start();
        System.out.println("Finished executing main thread: "+ Thread.currentThread().getName());*/

        // Implement using Java 8 Lambda
        /*System.out.println("Inside : " + Thread.currentThread().getName());
        Thread lambThread = new Thread(()->{System.out.println("Inside Lambda Runnable: " + Thread.currentThread().getName());});
        lambThread.start();
        System.out.println("Finished executing main thread: "+ Thread.currentThread().getName());
*/
    }
}
