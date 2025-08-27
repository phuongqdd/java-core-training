package queue_interface;

import java.util.*;

public class Cau1 {
    // Queue interface được mở rộng từ Collection interface
    // Đại diện cho hàng đợi, theo nguyên tắc FIFO
    // Đặc điểm chính:
    // Nguyên tắc FIFO:
    //      Thêm phần tử vào cuối(tail), lấy/xóa phần tử từ đầu(head)
    // Các phương thức cơ bản:
    //      offer(E e) – thêm phần tử vào queue, trả về false nếu thất bại.
    //      add(E e) – thêm phần tử, ném exception nếu thất bại.
    //      poll() – lấy và xóa phần tử đầu, trả về null nếu rỗng.
    //      remove() – lấy và xóa phần tử đầu, ném exception nếu rỗng.
    //      peek() – lấy phần tử đầu nhưng không xóa, trả về null nếu rỗng.
    //      element() – lấy phần tử đầu nhưng không xóa, ném exception nếu rỗng.
    //  Cho phép trùng lặp, có thể có null tùy implement.
    //  Thường dùng trong xử lý hàng đợi như task scheduling, in order, producer-consumer.
    //  Các lớp triển khai phổ biến:
    //      LinkedList (có thể dùng như Queue)
    //      PriorityQueue (queue theo thứ tự ưu tiên)
    //      ArrayDeque (double-ended queue, cũng implement Queue)
    //      ConcurrentLinkedQueue (thread-safe)


    // Qeque là viết tắt của Double-Ended Queue
    // Mở rộng từ Queue interface
    // Cho phép thêm/xóa phần tử ở cả hai ầu của danh sách
    // Đặc điểm:
    //      Hỗ trợ cả FIFO và LIFO
    //      Không giới hạn trùng lặp; null không được phép với hầu hết implement.
    //      Dùng để triển khai: queue kép, deque, stack, hoặc các thuật toán cần thêm/xóa ở cả hai đầu.
    //      Dùng để triển khai: queue kép, deque, stack, hoặc các thuật toán cần thêm/xóa ở cả hai đầu.
    // Các lớp triển khai phổ biến:
    //      LinkedList (có thể dùng như Deque)
    //      ArrayDeque (hiệu quả, nhanh hơn LinkedList)
    //      ConcurrentLinkedDeque (thread-safe)

    public static void main(String[] args) {

        System.out.println("=== Demo Queue: Hàng đợi FIFO ===");
        // Ví dụ: hàng đợi xử lý nhiệm vụ
        Queue<String> taskQueue = new LinkedList<>();
        taskQueue.add("Task1");
        taskQueue.add("Task2");
        taskQueue.add("Task3");

        System.out.println("Hàng đợi nhiệm vụ: " + taskQueue);
        System.out.println("Xử lý nhiệm vụ đầu tiên: " + taskQueue.poll()); // Lấy và xóa
        System.out.println("Hàng đợi sau khi xử lý: " + taskQueue);
        System.out.println("Xem nhiệm vụ tiếp theo: " + taskQueue.peek()); // Chỉ xem, không xóa

        System.out.println("\n=== Demo Deque: Queue kép và Stack LIFO ===");
        // Ví dụ: deque có thể dùng như stack hoặc queue kép
        Deque<String> deque = new ArrayDeque<>();

        // Thêm phần tử vào cả hai đầu
        deque.addFirst("Front1"); // đầu
        deque.addLast("Rear1");   // cuối
        deque.addFirst("Front2");
        deque.addLast("Rear2");
        System.out.println("Deque hiện tại: " + deque);

        // Xử lý FIFO: lấy phần tử đầu
        System.out.println("Lấy FIFO (đầu): " + deque.pollFirst());
        System.out.println("Deque sau FIFO: " + deque);

        // Xử lý LIFO: lấy phần tử cuối
        System.out.println("Lấy LIFO (cuối): " + deque.pollLast());
        System.out.println("Deque sau LIFO: " + deque);

        // Duyệt deque
        System.out.println("\nDuyệt deque còn lại:");
        for (String s : deque) {
            System.out.println(s);
        }

        System.out.println("\n=== Demo PriorityQueue: Hàng đợi ưu tiên ===");
        PriorityQueue<Integer> priorityQueue = new PriorityQueue<>();
        priorityQueue.add(50);
        priorityQueue.add(10);
        priorityQueue.add(30);
        priorityQueue.add(20);
        System.out.println("PriorityQueue: " + priorityQueue);
        System.out.println("Lấy phần tử ưu tiên (nhỏ nhất): " + priorityQueue.poll());
        System.out.println("PriorityQueue sau khi lấy: " + priorityQueue);
    }
}
