package sys_asy;
// Toàn bộ phương thức được khóa (lock) trên đối tượng hiện tại (this).
// Chỉ một thread được chạy phương thức này tại một thời điểm.
// Nếu bỏ synchronized, kết quả có thể < 2000 do race condition.
class Counter {
    private int count = 0;

    // synchronized method
    public synchronized void increment() {
        count++;
    }

    public synchronized int getCount() {
        return count;
    }
}
public class SyncMethodDemo {
    public static void main(String[] args) throws InterruptedException {
        Counter counter = new Counter();

        // 2 threads cùng tăng count
        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });

        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 1000; i++) counter.increment();
        });

        t1.start();
        t2.start();
        t1.join();
        t2.join();

        System.out.println("Final Count = " + counter.getCount()); // Luôn = 2000
    }
}
