package sys_asy;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AsyncDemo {
    static void doWork(String taskName){
        System.out.println(taskName + " bắt đầu");
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println(taskName + " kết thúc.");
    }

    public static void main(String[] args) throws InterruptedException {
        long start = System.currentTimeMillis();

        ExecutorService excecutor = Executors.newFixedThreadPool(3);

        excecutor.submit(() -> doWork("Task 1"));
        excecutor.submit(() -> doWork("Task 2"));
        excecutor.submit(() -> doWork("Task 3"));
        excecutor.shutdown();
        excecutor.awaitTermination(10, TimeUnit.SECONDS);

        long end = System.currentTimeMillis();
        System.out.println("Thổng thời gian: " + (end - start) / 1000 + " giây");

    }
}
