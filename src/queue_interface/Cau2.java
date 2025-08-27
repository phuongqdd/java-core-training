package queue_interface;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class Cau2 {

    // | Trường hợp sử dụng                         | Class thường dùng                             |
    //| ------------------------------------------ | --------------------------------------------- |
    //| Queue/Deque đơn luồng, nhanh               | ArrayDeque                                    |
    //| Queue/Deque đơn giản, linh hoạt            | LinkedList                                    |
    //| Multi-thread, không blocking               | ConcurrentLinkedQueue / ConcurrentLinkedDeque |
    //| Multi-thread, blocking (producer-consumer) | LinkedBlockingQueue / LinkedBlockingDeque     |
    //| Hàng đợi ưu tiên                           | PriorityQueue / PriorityBlockingQueue         |

    public static void main(String[] args) {

        System.out.println("=== 1. Queue với LinkedList (FIFO) ===");
        Queue<String> taskQueue = new LinkedList<>();
        taskQueue.add("Task1");
        taskQueue.add("Task2");
        taskQueue.add("Task3");
        System.out.println("Hàng đợi nhiệm vụ: " + taskQueue);
        System.out.println("Xử lý nhiệm vụ đầu tiên: " + taskQueue.poll());
        System.out.println("Hàng đợi sau khi xử lý: " + taskQueue);

        System.out.println("\n=== 2. PriorityQueue (FIFO theo ưu tiên) ===");
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(50);
        priorityQueue.add(10);
        priorityQueue.add(30);
        priorityQueue.add(20);
        System.out.println("PriorityQueue: " + priorityQueue);
        System.out.println("Lấy phần tử ưu tiên (nhỏ nhất): " + priorityQueue.poll());
        System.out.println("PriorityQueue sau khi lấy: " + priorityQueue);

        System.out.println("\n=== 3. Deque với ArrayDeque ===");
        Deque<String> deque = new ArrayDeque<>();
        deque.addFirst("Front1"); // Thêm đầu
        deque.addLast("Rear1");   // Thêm cuối
        deque.addFirst("Front2");
        deque.addLast("Rear2");
        System.out.println("Deque hiện tại: " + deque);

        // FIFO: lấy từ đầu
        System.out.println("Lấy FIFO (đầu): " + deque.pollFirst());
        System.out.println("Deque sau FIFO: " + deque);

        // LIFO: lấy từ cuối
        System.out.println("Lấy LIFO (cuối): " + deque.pollLast());
        System.out.println("Deque sau LIFO: " + deque);

        System.out.println("\n=== 4. ConcurrentLinkedQueue (thread-safe) ===");
        Queue<String> concurrentQueue = new ConcurrentLinkedQueue<>();
        concurrentQueue.add("User1");
        concurrentQueue.add("User2");
        concurrentQueue.add("User3");
        System.out.println("ConcurrentLinkedQueue: " + concurrentQueue);
        System.out.println("Xử lý User đầu tiên: " + concurrentQueue.poll());

        System.out.println("\n=== 5. ConcurrentLinkedDeque (thread-safe, FIFO/LIFO) ===");
        Deque<String> concurrentDeque = new ConcurrentLinkedDeque<>();
        concurrentDeque.addFirst("TaskFront");
        concurrentDeque.addLast("TaskRear");
        System.out.println("ConcurrentLinkedDeque: " + concurrentDeque);
        System.out.println("Lấy FIFO (đầu): " + concurrentDeque.pollFirst());
        System.out.println("Lấy LIFO (cuối): " + concurrentDeque.pollLast());

        System.out.println("\n=== 6. LinkedBlockingDeque (blocking queue) ===");
        LinkedBlockingDeque<String> blockingDeque = new LinkedBlockingDeque<>();
        try {
            blockingDeque.putFirst("Job1");
            blockingDeque.putLast("Job2");
            System.out.println("LinkedBlockingDeque: " + blockingDeque);
            System.out.println("Lấy phần tử đầu (blocking): " + blockingDeque.takeFirst());
            System.out.println("Lấy phần tử cuối (blocking): " + blockingDeque.takeLast());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
