package ru.bulgakov;

public class Main {
    public static void main(String[] args) {
        CustomThreadPool threadPool = new CustomThreadPool(6);

        for (int i = 0; i < 10; i++) {
            final int taskId = i;
            threadPool.execute(() -> {
                System.out.println("Task " + taskId + " is running in " + Thread.currentThread().getName());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                System.out.println("Task " + taskId + " is completed in " + Thread.currentThread().getName());
            });
        }

        threadPool.shutdown();

        threadPool.awaitTermination();

        System.out.println("All tasks are completed.");
    }
}
