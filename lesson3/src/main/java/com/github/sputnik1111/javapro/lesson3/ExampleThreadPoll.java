package com.github.sputnik1111.javapro.lesson3;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;
import java.util.stream.IntStream;

public class ExampleThreadPoll {

    private final LinkedList<Runnable> tasks = new LinkedList<>();

    private final Set<Thread> threads = new HashSet<>();

    private volatile boolean shutdown = false;

    private final Object monitor = new Object();

    public ExampleThreadPoll(int numPoll) {
        if (numPoll < 1) throw new IllegalArgumentException("numPoll must be more than 0 ");
        runThreads(numPoll);
    }

    public void execute(Runnable r) {
        if (r == null) throw new IllegalArgumentException("task is null");
        if (shutdown) throw new IllegalStateException("thread pool is shutdown");
        synchronized (monitor) {
            tasks.add(r);
            monitor.notify();
        }
    }

    public synchronized void shutdown() {
        if (shutdown) return;
        shutdown = true;
        tasks.clear();
        threads.forEach(Thread::interrupt);

    }

    private void runThreads(int numPoll) {
        IntStream.range(1, numPoll)
                .forEach(i -> {
                    Thread thread = new Thread(new ThreadLoop());
                    thread.start();
                    threads.add(thread);
                });
    }

    private class ThreadLoop implements Runnable {
        @Override
        public void run() {

            while (!shutdown) {
                Runnable r = null;
                synchronized (monitor) {
                    if (tasks.isEmpty()) {
                        try {
                            monitor.wait();
                            r = tasks.poll();
                        } catch (InterruptedException e) {
                            //ignore
                        }
                    }
                }
                if (r != null) {
                    try {
                        r.run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }

            System.out.printf(
                    "Thread %s finished %n",
                    Thread.currentThread().getName()
            );

        }
    }
}
