package ru.bulgakov;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

public class CustomThreadPool {
    private final WorkerThread[] threads;
    private final Queue<Runnable> taskQueue;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition taskAvailable = lock.newCondition();

    private volatile boolean isShutdown = false;

    public CustomThreadPool(final int capacity) {
        if (capacity <= 0) {
            throw new IllegalArgumentException("capacity must be > 0");
        }
        this.taskQueue = new LinkedList<>();
        this.threads = new WorkerThread[capacity];

        for (int i = 0; i < capacity; i++) {
            threads[i] = new WorkerThread();
            threads[i].start();
        }
    }

    public void execute(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            if (isShutdown) {
                throw new IllegalStateException("ThreadPool is shut down, cannot accept new tasks.");
            }
            taskQueue.add(task);
            taskAvailable.signal();
        } finally {
            lock.unlock();
        }
    }

    public void shutdown() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            isShutdown = true;
            taskAvailable.signalAll();
        } finally {
            lock.unlock();
        }
    }

    public void awaitTermination() {
        final ReentrantLock lock = this.lock;
        lock.lock();
        try {
            while (!taskQueue.isEmpty() || !isAllThreadsIdleLocked()) {
                try {
                    taskAvailable.await();
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        } finally {
            lock.unlock();
        }
    }

    private boolean isAllThreadsIdleLocked() {
        for (WorkerThread thread : threads) {
            if (thread.busy) {
                return false;
            }
        }
        return true;
    }

    private class WorkerThread extends Thread {
        private boolean busy = false;

        @Override
        public void run() {
            for (;;) {
                Runnable task;
                lock.lock();
                try {
                    while (taskQueue.isEmpty() && !isShutdown) {
                        try {
                            taskAvailable.await();
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            return;
                        }
                    }

                    if (taskQueue.isEmpty() && isShutdown) {
                        return;
                    }

                    task = taskQueue.poll();
                    busy = true;
                } finally {
                    lock.unlock();
                }

                try {
                    task.run();
                } finally {
                    lock.lock();
                    try {
                        busy = false;
                        if (taskQueue.isEmpty() && isAllThreadsIdleLocked()) {
                            taskAvailable.signalAll();
                        }
                    } finally {
                        lock.unlock();
                    }
                }
            }
        }
    }
}