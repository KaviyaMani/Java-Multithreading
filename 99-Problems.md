### Producer Consumer
    Problem -
        If producer is producing more, data will overflow can't store more than limit
        If consumer is consuming more, data won't be there to consume
    Solution -
        If Queue is not full then only producer can produce item
        If Queue is not empty then only consumer can consume item
    Approach -
        Only 1 process should occur at a time for data consistency- use synchronized on both
        Producer -  If Queue is full thread should wait, release lock for others so conumer can consume - check if size reached then wait
                    If Queue is not full then produce and notify for other threads to consume if it is waiting
        Consumer -  If Queue is empty thread should wait, release lock for others so producer can produce
                    If Queue is not empty, consumer can consume and release lock for others to produce or consume
    Refer ProducerConsumerWithQueue.java - with synchronized
        If you reduce sleep time for producer, producer will wait for consumer to consume
        If you reduce sleep time for consumer, consumer will wait for producer to produce more
    Refer ConditionInLockExample.java - with locks and condition