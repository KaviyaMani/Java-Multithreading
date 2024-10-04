package com.nila.concurrency.sync;

public class StaticVsNonStaticMethod {
    private static int staticCounter = 0;
    private int nonStaticCounter = 0;

    public static synchronized void staticMethod() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("Static Method: " + (++staticCounter));
        }
    }

    public synchronized void nonStaticMethod() {
        for (int i = 0; i < 1000; i++) {
            System.out.println("Non-Static Method: " + (++nonStaticCounter));
        }
    }

    public static void main(String[] args) {
        StaticVsNonStaticMethod example = new StaticVsNonStaticMethod();

        Runnable staticTask = () -> {
            example.staticMethod();
        };

        Runnable nonStaticTask = () -> {
            example.nonStaticMethod();
        };

        Thread staticThread = new Thread(staticTask, "Static Thread");
        Thread nonStaticThread = new Thread(nonStaticTask, "Non Static Thread");

        staticThread.start();
        nonStaticThread.start();

        try {
            staticThread.join();
            nonStaticThread.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
