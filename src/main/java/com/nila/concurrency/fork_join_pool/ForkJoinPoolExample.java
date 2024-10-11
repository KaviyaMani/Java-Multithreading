package com.nila.concurrency.fork_join_pool;


import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.RecursiveAction;
import java.util.concurrent.RecursiveTask;

class  MyRecursiveAction extends RecursiveAction {

    private long workLoad = 0;

    public MyRecursiveAction(long workLoad) {
        this.workLoad = workLoad;
    }

    @Override
    protected void compute() {
    //if work is above threshold, break tasks up into smaller tasks
        if(this.workLoad > 16) {
            System.out.println("MyRecursiveAction Splitting workLoad : " + this.workLoad);

            long workload1 = this.workLoad / 2;
            long workload2 = this.workLoad - workload1;

            MyRecursiveAction subtask1 = new MyRecursiveAction(workload1);
            MyRecursiveAction subtask2 = new MyRecursiveAction(workload2);

            subtask1.fork();
            subtask2.fork();

        } else {
            System.out.println("MyRecursiveAction Doing workLoad myself: " + this.workLoad);
        }
    }
}
public class ForkJoinPoolExample {

    public static void main(String[] args) {
//        ForkJoinPool forkJoinPool = ForkJoinPool.commonPool();
        ForkJoinPool forkJoinPool = new ForkJoinPool(4);
        MyRecursiveAction myRecursiveAction = new MyRecursiveAction(66);

        forkJoinPool.invoke(myRecursiveAction);

        MyRecursiveTask myRecursiveTask = new MyRecursiveTask(128);

        long mergedResult = forkJoinPool.invoke(myRecursiveTask);

        System.out.println("mergedResult = " + mergedResult);
    }
}

class MyRecursiveTask extends RecursiveTask<Long> {

    private long workLoad = 0;

    public MyRecursiveTask(long workLoad) {
        this.workLoad = workLoad;
    }

    protected Long compute() {
        //if work is above threshold, break tasks up into smaller tasks
        if(this.workLoad > 16) {
            System.out.println("MyRecursiveTask Splitting workLoad : " + this.workLoad);

            long workload1 = this.workLoad / 2;
            long workload2 = this.workLoad - workload1;

            MyRecursiveTask subtask1 = new MyRecursiveTask(workload1);
            MyRecursiveTask subtask2 = new MyRecursiveTask(workload2);

            subtask1.fork();
            subtask2.fork();

            long result = 0;
            result += subtask1.join();
            result += subtask2.join();
            return result;

        } else {
            System.out.println("MyRecursiveTask Doing workLoad myself: " + this.workLoad);
            return workLoad * 3;
        }
    }
}
