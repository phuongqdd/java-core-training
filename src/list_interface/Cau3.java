package list_interface;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cau3 {
    public static void main(String[] args) {

        System.out.println("=== 1. ArrayList: truy xuất nhanh, ít thêm/xóa ===");
        // Ví dụ: danh sách sản phẩm để hiển thị
        List<String> products = new ArrayList<>();
        products.add("Laptop");
        products.add("Smartphone");
        products.add("Tablet");
        products.add("Laptop"); // trùng lặp được phép
        System.out.println("Danh sách sản phẩm: " + products);
        System.out.println("Truy xuất sản phẩm thứ 2: " + products.get(1));

        System.out.println("\n=== 2. LinkedList: thêm/xóa nhiều, dùng Queue/Deque ===");
        // Ví dụ: hàng đợi nhiệm vụ
        LinkedList<String> taskQueue = new LinkedList<>();
        taskQueue.addLast("Task1");
        taskQueue.addLast("Task2");
        taskQueue.addLast("Task3");
        System.out.println("Hàng đợi nhiệm vụ: " + taskQueue);
        System.out.println("Thêm nhiệm vụ ưu tiên đầu hàng đợi");
        taskQueue.addFirst("UrgentTask");
        System.out.println("Hàng đợi sau khi thêm: " + taskQueue);
        System.out.println("Xử lý nhiệm vụ đầu tiên: " + taskQueue.removeFirst());
        System.out.println("Hàng đợi còn lại: " + taskQueue);

        System.out.println("\n=== 3. Vector: thread-safe trong môi trường đa luồng ===");
        // Ví dụ: danh sách đồng bộ trong đa luồng
        List<String> vector = new Vector<>();
        vector.add("User1");
        vector.add("User2");
        vector.add("User3");
        System.out.println("Vector chứa người dùng: " + vector);

        System.out.println("\n=== 4. Stack: LIFO (undo/redo) ===");
        // Ví dụ: undo/redo thao tác
        Stack<String> history = new Stack<>();
        history.push("Trang1");
        history.push("Trang2");
        history.push("Trang3");
        System.out.println("Lịch sử: " + history);
        System.out.println("Undo thao tác: " + history.pop());
        System.out.println("Lịch sử sau undo: " + history);

        System.out.println("\n=== 5. CopyOnWriteArrayList: đọc nhiều, ghi ít trong đa luồng ===");
        // Ví dụ: danh sách người online, nhiều thread đọc
        CopyOnWriteArrayList<String> onlineUsers = new CopyOnWriteArrayList<>();
        onlineUsers.add("Alice");
        onlineUsers.add("Bob");
        onlineUsers.add("Charlie");
        System.out.println("Người online: " + onlineUsers);

        System.out.println("\nDuyệt người online (giả lập nhiều đọc):");
        for (String user : onlineUsers) {
            System.out.println(user);
        }
    }
}
