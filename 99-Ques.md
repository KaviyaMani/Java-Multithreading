#### Why wait, notify, notifyAll in Object class instead of Thread class?
    Since the lock is put and release on Object not on Thread
#### How concurrent collections achieve thread safety?
    By using synchronized or Reentrant Lock or Compare and Swap
#### What is ideal pool size?
    Since no. of threads can run at a time is based on how many CPU we have, it is ideal to thread pool
        size same as No. of CPUs. 2 or 4 based on machine
    Min and max value depend on, CPU cores, JVM memory, Memory requirement of task
#### Why Thread.stop(), Thread.suspend(), Thread.resume deprecated?
    They leave abruptly without releasing locks so other thread wolud starve due to that
#### In Thread pool, why putting the task to queue once threads are busy and only create max threads only queue is full?
    Min thread is sufficient for most of the scenarios, max thread is only for peak time. If we create more threads in the 
    beginning after peak hour obviously not needed hence it has to be removed. So it should be created only when it is 
    absolutely needed