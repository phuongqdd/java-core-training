package sys_asy;
// Áp dụng cho phương thức tĩnh → lock nằm ở class object (ClassName.class), không phải instance.
// Nếu có nhiều instance, chúng vẫn cùng chia sẻ một lock.

class StaticCounter {
    private static int count = 0;

    // synchronized static method
    public static synchronized void increment() {
        count++;
    }

    public static synchronized int getCount() {
        return count;
    }
}

public class StaticSyncDemo {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) StaticCounter.increment();
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) StaticCounter.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final Count = " + StaticCounter.getCount());
    }
}
