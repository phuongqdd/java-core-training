package string;

public class StringBufferDemo {
    public static void main(String[] args) throws InterruptedException {
        StringBuffer logBuffer = new StringBuffer("Log start\n");

        Runnable task1 = () -> {
            logBuffer.append("Thread 1: login\n");
            logBuffer.append("Thread 1: action\n");
        };

        Runnable task2 = () -> {
            logBuffer.append("Thread 2: login\n");
            logBuffer.append("Thread 2: action\n");
        };

        Thread t1 = new Thread(task1);
        Thread t2 = new Thread(task2);

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println(logBuffer.toString());
    }
}
