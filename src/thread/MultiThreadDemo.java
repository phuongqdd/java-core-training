package thread;

class MyTask extends Thread {
    private String name;

    public MyTask(String name) {
        this.name = name;
    }

    @Override
    public void run() {
        for(int i = 1; i <= 5; i++) {
            System.out.println(name + " - bước " + i);
            try {
                Thread.sleep(500); // Giả lập xử lý mất 0.5s
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}

public class MultiThreadDemo {
    public static void main(String[] args) {
        MyTask t1 = new MyTask("Thread 1");
        MyTask t2 = new MyTask("Thread 2");

        t1.start(); // chạy song song
        t2.start();

        System.out.println("Main thread vẫn chạy tiếp...");

        System.out.println("hi hi");
    }
}
