package thread;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ThreadDemo {
    public static void main(String[] args) throws InterruptedException, ExecutionException {
        Thread t1 = new Thread(() -> {
            System.out.println("Thread 1 đang chạy ....");
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("Thread 1 hoan thành!");
        });

//        t1.start();
//
//        t1.join();

        System.out.println("Main thread biết rằng t1 đã xong!");

        ExecutorService executor = Executors.newFixedThreadPool(3);

        Future<String> f1 = executor.submit(() -> {
            Thread.sleep(1000);
            return "Task 1 done";
        });

        Future<String> f2 = executor.submit(() -> {
            Thread.sleep(2000);
            return "Task 2 done";
        });

        Future<String> f3 = executor.submit(() -> {
            Thread.sleep(1500);
            return "Task 3 done";
        });
        System.out.println(f3.get());
        System.out.println(f1.get()); // chờ task 1
        System.out.println(f2.get()); // chờ task 2
        System.out.println(f3.get());
        System.out.println(f1.get()); // chờ task 1
        System.out.println(f2.get());// chờ task 3

        executor.shutdown();

    }
}
