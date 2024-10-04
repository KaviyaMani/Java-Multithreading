## Lock - interface
    Do not depend on object. Just that only 1 thread can enter the lock thats it.  
    java.util.concurrent.lock - Locks are created using synchronized blocks
    Same as synchronized but provides a sophisticated way of acheiving synchronization, plus it provides additional methods
    Lock interface represents a concurrent lock which make sure only one thread at a time can lock the lock, 
    perform its critical logic atomically, and unlock the lock again. 
    **Lock can be reentrant**, meaning the same thread can lock the Lock more than one time. 
    The Lock must then be unlocked the same number of times before it is fully unlocked for other threads.\
    Lock can guarantee fairness among threads waiting to lock the Lock, meaning the threads will be guaranteed to be 
        allowed to lock the Lock in the same sequence the threads called the Lock.lock() method. 
        This prevents starvation, where a waiting thread is never allowed to lock the Lock because other threads keep "passing" it in the waiting queue.
            Lock lock = new ReentrantLock(); 
            lock.lock();
            //do something
            lock.unlock();
    By doing this only 1 thread can execute this code between lock but remaining code can be accessible by other threads
    Have to be surrouned with try/finally. unlock in finally to ensure unlock happens even if error occurs

### Synchronized vs Locks
    Synchronized -
        Uses or depend on monitor lock of object
        does not support fairness, no preference can be specified - some thread can starve waiting
        thread gets blocked if it can’t get an access to the synchronized block
        thread that is in “waiting” state can’t be interrupted
        synchronized block must contain within methods
        synchronized block always renentrant
        happens before guarantee for synchronized block
        Having a timeout trying to get access to a synchronized block is not possible

    Locks - 
        Lock does not depend on object
        can achieve fairness within Lock APIs by fairness property. makes sure that the longest waiting thread is given access to the lock
        Lock API provides tryLock() method. The thread acquires lock only if it’s available and not held by any other thread. reduce waiting time
        Lock API provides a method lockInterruptibly() that can be used to interrupt the thread when it’s waiting for the lock.
        lock and unlock can be called from diff methods
        Lock could decide to be reentrant or not
        lock and unlock provides same visibility
        Using Lock.tryLock(long timeout, TimeUnit timeUnit), it is possible.

## Types
    Reentrant Lock
    ReadWrite Lock
    Stamped Lock
    Semaphore

### Reentrant Lock
Reentrance means, If a thread already holds the lock on a monitor object, it has access to all blocks synchronized on the same monitor object

    Scenario 1: If a thread which holds the lock try to get the lock again it can get only if the type of lock is Reentrant
        If the lock type is diff, lock will reach Reentrant lockout(deadlock) since thread will wait to get the lock which is hold by itself
        Lock should be unlocked n times, if it is locked n times then only other thread can get the lock
        **ReEntrantLockExample.java**
    By default reentrant lock does not provide fairness policy - new ReentrantLock(true)
    Just by providing true it make sure, thread get the lock based on order of arrival
    Scenario 2: When 2 diff objects share the same lock, only 1 object can use the resourse.
        This won't work with synchronized, since monitor lock is on object
        **ReEntrantSharedLock.java**

## Methods
    lockInterruptibly - similiar to lock , it allows blocked thread to interrupted and resume the execution(like break statement)
    **lock()** - acquire the lock if available, else wait for lock to be released
    **tryLock()** - non-blocking version of lock(), try to acquire lock if succeeds, return true - does not provide fairness guarantee
    tryLock(long timeout, TimeUnit timeUnit) - waits up the given timeout before giving up trying to acquire the Lock
    **unlock()** - unlocks the Lock instance.
    isFair(), isLocked(), isHeldByCurrentThread(), getHoldCount()(how many times locked), getQueueLength()(how many threads waiting), hasQueuedThreads(), hasQueuedThread(Thread.currentThread())

    tryLock() usecase: lock.tryLock(1, TimeUnit.SECONDS); if(isLockAcquired) { do critical function } lock.unlock()
The lock() method locks the Lock instance so that all threads calling lock() are blocked until unlock() is executed
    
### Read/Write Lock - multiple readers but only one writer
    Should be used when read is high, and write is very less
    ## Shared Lock - Read Lock
    ## Exclusive Lock - Write Lock
    ReentrantReadWriteLock - implements the ReadWriteLock interface.
    Consists of pair of locks - one for read access, another for write access. readLock, writeLock
    The read lock can be held by multiple threads but write lock can be held by only 1 thread
    Conditions to get access
        Read Access   	If no threads are writing, and no threads have requested write access.
        Write Access   	If no threads are reading or writing.
        T1, T2 - Allowed(SS) Not allowed(SX, XS, XX)
            Both can acquire shared lock,
            If shared lock is there, cannot acquire Exclusive lock
            If Exclusive lock is there, cannot acquire Shared lock
            If Exclusive Lock is there cannot acquire Exclusive lock
        That means, Since Exclusive lock can update the value it should be protected
    Prioritizing write-access since write requests are more important than read
    why prioritize? if reads are what happens most often, and we did not up-prioritize writes, **starvation** could occur
        Lock readLock() returns the lock that’s used for reading.
        Lock writeLock() returns the lock that’s used for writing.
            ReadWriteLock lock = new **ReentrantReadWriteLock**();
            Lock writeLock = **lock.writeLock()**;
            **writeLock.lock()**; do something **writeLock.unlock()**; in finally
    Refer: ReadWriteLockExample.java

#### Optimistic Lock -
    No lock acquired
    Example using DB -
        Extra @Version column attached to table, which keep track of changes. initially 1 keep on updating for each changes
        each transaction that reads data holds the value of the version property.
        Before the transaction wants to make an update, it checks the version property again.
        If the value has changed in the meantime, an OptimisticLockException is thrown.
        else, transaction commits the update and increments a value version property.
#### Pessimistic Lock -
    the pessimistic locking mechanism involves locking entities on the database level.
    Each transaction can acquire a lock on data. As long as it holds the lock, no transaction can read, 
        delete or make any updates on the locked data.
    We can presume that using pessimistic locking may result in deadlocks. However, 
        it ensures greater integrity of data than optimistic locking.
    
### StampedLock - In java 8
    There is no locking, tryOptimisticRead, if no update happened in the meantime use the value else error thats it
    Support Read/Write functionality + Optimistic read
        StampedLocks allow optimistic locking for read operations
        StampedLocks are not reentrant (ReentrantLocks are reentrant - if not used properly it leads to starvation)
    Involves 2 elements
        lock version - known as stamp - value of type “long” returned every time a lock is acquired
        locking mode - READ, WRITE, OPTIMISTIC READ
            Write Lock - after acquiring the lock, return stamp. stamp can be used to unlockWrite(long) to release the lock
            Read Lock -  readLock() return stamp, unlockRead(long) to release the lock 
            Optimistic Read Lock - advanced booking attempt for a read lock - may or may not be granted
                1. **tryOptimisticRead()** method returns a stamp only if the lock is not currently held in write mode.
                2. **validate(long)** method returns true if the lock has not been acquired in write mode since obtaining a given stamp.
                3. if the write lock has been acquired since – the optimistic read fails.
                4. No need to unlock, since we are not locking anything
    Other methods:
        tryConvertToWriteLock(long stamp)
        tryConvertToReadLock(long stamp)
        tryConvertToOptimisticRead(long stamp)
    Refer: StampedLockOptimisticRead.java

### Semaphore Lock
    to limit the number of concurrent threads accessing a specific resource.
    can be used to limit the number of users in the system
        **tryAcquire()** – return true if a permit is available immediately and acquire it otherwise return false, but acquire() acquires a permit and blocking until one is available
        **release()** – release a permit
        **availablePermits()** – return number of current permits available
    acquire(), release(), drainPermits(), 
    Semaphore lock = new Semaphore(2); lock.acquire(); //do something  lock.release();
        Since it permits 2 at a time 2 threads can access concurrently
    Refer: SemaPhoreExample.java

### Condition
    synchronized uses - monitor lock to work
    Locks use this condition for inter-thread communication
        await = wait
        signal = notify
        signalAll = notifyAll
    For these custom locks, these conditions are used instead of monitor locks
    These conditions are applied on lock not on objects
        ReentrantLock lock = new ReentrantLock();
        Condition condition = lock.newCondition();
        lock.lock(); condition.await(); condition.signal(); condition.signalAll(); lock.unlock(); based on scenario
    Refer: ConditionInLockExample.java