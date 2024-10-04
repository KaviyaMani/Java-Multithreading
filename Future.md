## Future
    When we want to know the status of async task submitted to pool
    return type of submit is Future<?>
    Interface which represents the result of async task
    using the result, check if it completed, get the result, handle exception
    methods:
        boolean cancel() - attempts to cancel the execution of task, returns false if cannot cancel(if it got completed already)
        boolean isCancelled() - returns true, if it got cancelled before completion
        boolean isDone() - task completed or not. success, cancellation or exception does not matter
        V get() - wait if required for the completion of task and return result if available. like **join**
        V get(long timeout, unit) - Timed waiting, after reached throws TimeoutException
    Refer - SimpleFuture.java

    whatever runnable task we are passing to pool, it get converted to Future task(Future+state)
        possible state - NEW = 0; COMPLETING = 1; NORMAL = 2; EXCEPTIONAL = 3;CANCELLED = 4;INTERRUPTING = 5;INTERRUPTED  = 6;
        based on state future provides result

we can submit by submit(Runnable), submit(Runnable, result), submit(Callable)
    
    submit(Runnable, result) - when runnable task uses shared array or list, by passing it in result
                                all the task will use this passed array or list. finally updated value will be returned with get()
    Ex: SimpleFutureRunnableWithResult.java
## Callable
    Same as runnable, represents the task to run
        - Runnable do not have any return type - void
        - Callable can have a return type - V
    Refer - SimpleFutureCallable.java
        Callable<List<Integer>> callable2 = new Callable<List<Integer>>() {
            @Override
            public List<Integer> call() throws Exception {
                return Arrays.asList(1,10,23,45,67,87);
            }
        };
        Future<?> task2 = pool.submit(callable2);
        Object task2Result = task2.get();
        // Task 2 result: [1, 10, 23, 45, 67, 87]

Runnable get() always return null since return type is void

## CompletableFuture
    Introduced in Java 8 to help in Asyn programming
    class which implements Future, CompletionStage(additional functionalities)
    advanced version of future 
    provides additional capability like chaining
    methods -
    1. **CompletableFuture.supplyAsync** - 1. supplyAsync(Supplier<U> supplier), 2.supplyAsync(Supplier<U> supplier, Executor executor)
        To initiate Sync operation(like submit()), supplier is executed asynchronously in separate thread
        By default it will use shared **ForkJoinPool** Executor for Thread pool. It dynamically adjusts pool size based on processors
        If we want more control over Executor, we can create our own and pass it
        uses Supplier functional interface, execute something and returns result
    CompletableFuture<Void> run = CompletableFuture.**runAsync**(()-> System.out.println("hello"));
        Takes Runnable as parameter instead of Supplier and returns Void
        when you don't need to return value from asynchronous operation
    join - blocking call causes the current thread to wait for CompletableFututre.
        future.join()
    2. thenApply() & thenApplyAsync() - chaining - 
    thenApply() - Applies a synchronous transformation function to the result of the CompletableFuture when it completes.
        Apply a function to the result of previous Async operation
        Returns new CompletableFuture
        Synchronous execution
        **uses the same thread** which executed supplyAsync task
    thenApplyAsync() - same executor should be provided here too else thread will be from fork join pool
        It is a asynchronous execution
        Uses different thread to execute(got the thread from fork join pool if custom executor not provided)
    3. thenCompose() & thenComposeAsync()
    thenCompose() -
        Chain together dependent Async operations
        when next Async operation depend on result of previous, we can tie them together
        **For Async tasks, we can bring ordering using this** - If Async tasks have to execute in particular order
        chain multiple supplyAsync() together, if it is related
        If multiple supplyAsync operation related to each other and **execute in order** and depend on previous result
    4. theAccept() & thenAcceptAsync()
    thenAccept() - 
        end Stage, in the chain of Async operations
        consumes the reult, It does not return anything
    5. thenCombine() & thenCombineAsync()
    thenCombine() -
        Used to combine the result of  exactly 2 completable futures when both future task completes
    6. thenRun() & thenRunAsync()
        Executes a Runnable action when the CompletableFuture completes, without using the result of the future. 
        thenRun is synchronous, while thenRunAsync is asynchronous
    7. exceptionally - Handles exceptions arising from the CompletableFuture computation, 
                        allowing for a fallback value to be provided or a new exception to be thrown.
    8. handle and handleAsync - 
        Applies a function to the result or exception of the CompletableFuture. 
        The synchronous variant is handle, and the asynchronous variant is handleAsync.

**CompletableFuture.allOf(task1, task2).join();** - Wait to complete all tasks \
Refer: CompletableFutureThenApply.java, CompletableFutureThenCompose, CompletableFutureThenAccept, CompletableFutureThenCombine
Refer: https://pwrteams.com/content-hub/blog/async-programming-and-completablefuture-in-java#:~:text=join(),task%20represented%20by%20the%20CompletableFuture.