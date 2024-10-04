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
            returns shared fork join pool, this way whole app uses same pool
        - new ForkJoinPool(4)
            your own, 4 - no of threads
            4 - just metioning what we need, but it may increase based on need to avoid waiting of tasks
    2. pool.invoke(task)

RecursiveTask
RecursiveAction