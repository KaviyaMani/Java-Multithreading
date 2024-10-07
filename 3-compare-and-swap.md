## Concurrency
    Can be achieved in 2 ways
        Lock Based mechanism - synchronied, reentrant, readwrite, stamped, semaphone
        Lock free mechanism - CAS operation - Compare And Swap
            AtomicInteger, AtomicBoolean, AtomicLong, AtomicReference
    Usecase -
        Lock based - slow since locking mechanism, on complex scenario, 
        Lock free - fast since no additional mechanism, only for specific scenarios
    Lock free is not an altenative to lock based, just a way for specific scenario
#### ABA Problem
    The ABA problem occurs during synchronization: a memory location is read twice and has the same value for both reads. 
    However, another thread has modified the value, performed other work, then modified the value back between the two reads, 
    thereby tricking the first thread into thinking that the value never changed.
### Compare And Swap
    Similiar to optimistic locking
    Low level operation(supported by CPU), Atomic, All modern processor supports it, ABA problem can be solved by adding version
    Invloves 3 parameters,
        memory location - location where data is stored
        Expected value - value should be stored in memory, same as version number or stamp in lock
        New value - value to be written memory, if current value matches expected value

## Atomic Variable
    All or nothing - protects underlying value by providing methods and perform atomic operations using CAS.
    int value = 10; - Atomic operation,  value++; - Non Atomic Operation(load value, add value, update value)
    can solve above issue by synchronized or AtomicInteger
    AtomicInteger - **uses CAS and volatile to acheive atomicity**
        1. Get the value from memory since volatile
        2. Update the value
        3. check the value with version it have, if the version is same update the value
            if the version updated, **start again from step 1**
    Refer: AtomicVariable.java
Note: only when we need read, modify, update go for atomic else choose lock mechanism

#### Atomic vs volatile
    Atomic -
        Thread safe
        Solution for consistency
    Volatile -
        No relation with thread safety  
        solution for visibility problem

#### Concurrent collection
    Internally uses synchronized or reentrant lock or compare and swap to achieve thread safety