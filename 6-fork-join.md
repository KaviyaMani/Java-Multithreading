### Fork/Join Pool Framework
    provides tools to help speed up parallel processing by attempting to use all available processor cores. 
    It accomplishes this through a divide and conquer approach.
    1. forks() - recursively breaking the task into smaller independent subtasks until they are simple enough to run asynchronously
    2. join() - The results of all subtasks are recursively joined into a single result.
                for task that returns void, the program simply waits until every subtask runs.
    To acheive this fork/join framework uses ForkJoinPool. This pool manages ForkJoinWorkerThread
    It increases parallelism
#### ForkJoinPool
    Implementation of ExecutorService, to manage worker threads. can get info about thread pool state and performance
    Worker threads can execute only 1 task at a time, won't create separate thread for each subtask
    Each thread in the pool has **own double ended queue(deque) to store tasks**
    Queue used for balancing workload with the help of work-stealing algorithm
#### Work-Stealing Algorithm
    free threads try to “steal” work from deques of busy threads.
    By default, a worker thread gets tasks from the head of its own deque.
    When it is empty, the thread takes a task from the tail of the deque of another busy thread or
        from the global entry queue since this is where the biggest pieces of work are likely to be located
    This approach minimizes the possibility that threads will compete for tasks.
    It also reduces the number of times the thread will have to go looking for work, as it works on the biggest available chunks of work first.

### Ways to create
    1. Using ForkJoinPool 
        - ForkJoinPool.commomPool() 
            returns shared fork join pool, this way whole app uses same pool,
            parallelism depends on the workload submitted to it 
        - new ForkJoinPool(4)
            your own, 4 - no of threads
            4 - just metioning what we need, but it may increase based on need to avoid waiting of tasks
    2. pool.invoke(task)

### Ways to submit task to Pool
    2 types of tasks can be submitted
    1. RecursiveAction - task that does not return any result (an "action")
        - It just does some work, e.g. writing data to disk, and then exits.
        1. create a subclass of it, public class MyRecursiveAction extends RecursiveAction
        2.   @Override  protected void compute() {
        3. If a task is small this MyRecursiveAction will handle else
        4. It will create some more action and delegate the tasks to it
            If the workLoad is above a certain threshold, the work is split into subtasks which are also scheduled for 
            execution (via the .fork() method)
        5. forkJoinPool.invoke(myRecursiveAction);
        
    2. RecursiveTask - task which does return a result (a "task")
        It may split its work up into smaller tasks, and merge the result of these smaller tasks 
            into a collective result
        The splitting and merging may take place on several levels
        1. Create subclass of RecursiveTask<Type>
        2. override protected Long compute() {
        3. If a task is small MyRecursiveTask will handle else
        4. breaks the work down into subtasks, and schedules these subtasks for execution 
            using their fork() method.
        5. then receives the result returned by each subtask by calling the join() method of each subtask.
        6. This kind of joining / mergining of subtask results may occur recursively for several levels of recursion.
        7. long mergedResult = forkJoinPool.invoke(myRecursiveTask);

    RecursiveAction and the RecursiveTask classes are subclasses of the ForkJoinTask class
    Refer: ForkJoinPoolExample