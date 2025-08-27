package sys_asy;

// Chỉ một phần code được đồng bộ hóa thay vì cả phương thức.
// Hiệu suất tốt hơn, vì không phải khóa toàn bộ phương thức.
// Ưu điểm: Bạn có thể chọn lock riêng cho từng phần code → tránh deadlock & tăng hiệu suất.
class CounterBlock {
    private int count = 0;
    private final Object lock = new Object();

    public void increment() {
        // chỉ đồng bộ hóa đoạn này
        synchronized (lock) {
            count++;
        }
    }

    public int getCount() {
        synchronized (lock) {
            return count;
        }
    }
}

public class SyncBlockDemo {
    public static void main(String[] args) throws InterruptedException {
        CounterBlock counter = new CounterBlock();

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
