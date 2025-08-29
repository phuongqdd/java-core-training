package thread;

class MyThread extends Thread {
    @Override
    public void run() {
        System.out.println("Thread chạy bằng cách extends Thread");
    }
}

// Ưu điểm: Đơn giản, dễ hiểu.
//Nhược điểm: Java không hỗ trợ đa kế thừa → nếu đã extends Thread thì không thể extends class khác.

class MyRunnable implements Runnable {
    @Override
    public void run() {
        System.out.println("Thread chạy bằng cách implements Runnable");
    }
}

// Linh hoạt hơn vì Java hỗ trợ implement nhiều interface.
// Phù hợp khi class đã kế thừa class khác.

public class CacCachKhoiTao {
    public static void main(String[] args) {
        MyThread t1 = new MyThread();
        t1.start();

        Thread t2 = new Thread(new MyRunnable());
        t2.start();
    }
}
