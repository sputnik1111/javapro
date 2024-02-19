package com.github.sputnik1111.javapro.lesson3;


public class ExampleApp {
    public static void main(String[] args) throws InterruptedException {
        ExampleThreadPoll threadPoll = new ExampleThreadPoll(5);

        threadPoll.execute(() -> runTask("Task 1", 1000));
        threadPoll.execute(() -> runTask("Task 2", 4000));
        threadPoll.execute(() -> runTask("Task 3", 3000));

        threadPoll.execute(() -> runTask("Task 4", 20000));

        Thread.sleep(5000);

        threadPoll.shutdown();
    }

    private static void runTask(String taskName, long mills) {
        try {
            System.out.printf(
                    "Thread %s started task %s %n",
                    Thread.currentThread().getName(),
                    taskName
            );
            Thread.sleep(mills);
            System.out.printf(
                    "Thread %s finished task %s %n",
                    Thread.currentThread().getName(),
                    taskName
            );
        } catch (InterruptedException e) {
            System.out.printf(
                    "Thread %s interrupted for task %s %n",
                    Thread.currentThread().getName(),
                    taskName
            );
            throw new RuntimeException(e);
        }
    }
}
