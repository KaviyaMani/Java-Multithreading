## Task Executor
    Spring provides the TaskExecutor as an abstraction for dealing with executors
    We want our threads to be managed by Spring, also want to shut down our application gracefully, without any work being in progress.
    TaskExecutor interface is identical to the Executor interface