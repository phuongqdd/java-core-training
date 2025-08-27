package list_interface;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Cau1 {
    // List là một interface trong java, nó có thể chứa các phần tử trùng nhau hoặc phần tử null.
    //      List chứa các phương thức dựa trên chỉ số (index) để chèn, cập nhật, xóa và tìm kiếm các phần tử

    // Có thể xem như 1 dạng mảng nhưng chứa các phương thức phong phú hon
    // Không cố định kích thức như mảng
    // Có 3 class con: ArrayLisst, LinkedList, Vector
    // # Các đặc điểm
    // - Ordered (Có thứ tự)
    //      Phần tử được lưu theo thứ tự thêm vào.
    //      Có thể truy xuất phần tử bằng index: list.get(index).
    // - Cho phép trùng lặp (Duplicates allowed)
    //      Không giống Set, List có thể chứa nhiều phần tử giống nhau.
    // - Cho phép null (tùy implement)
    //      ArrayList cho phép nhiều giá trị null.
    //      LinkedList cũng cho phép null.
    // - Index-based access (Truy xuất theo chỉ số)
    //      Truy cập nhanh các phần tử qua index: get(index), set(index, element).
    //      Chèn phần tử ở bất kỳ vị trí nào: add(index, element).
    // - Kích thước có thể thay đổi (Resizable)
    //      Với các implement như ArrayList hay LinkedList, List có thể tự động mở rộng khi thêm phần tử.
    // - Có thể lặp (Iterable)
    //      Hỗ trợ for-each loop và Iterator để duyệt phần tử.

    public static void main(String[] args) {
        // 1. Tạo một List bằng ArrayList
        List<String> arrayList = new ArrayList<>();

        // Thêm phần tử (add)
        arrayList.add("Java");
        arrayList.add("Python");
        arrayList.add("C++");
        arrayList.add("Java"); // trùng lặp được phép
        arrayList.add(null);   // null cũng được phép

        // Truy xuất theo index (get)
        System.out.println("Phần tử thứ 2 của ArrayList: " + arrayList.get(1));

        // Thay thế phần tử (set)
        arrayList.set(2, "C#");
        System.out.println("ArrayList sau khi thay thế: " + arrayList);

        // Chèn phần tử vào vị trí cụ thể
        arrayList.add(1, "JavaScript");
        System.out.println("ArrayList sau khi add(1, \"JavaScript\"): " + arrayList);

        // Xóa phần tử theo index
        arrayList.remove(3);
        System.out.println("ArrayList sau khi remove(3): " + arrayList);

        // Lấy kích thước
        System.out.println("Kích thước ArrayList: " + arrayList.size());

        // Duyệt ArrayList bằng for-each
        System.out.println("Duyệt ArrayList:");
        for (String lang : arrayList) {
            System.out.println(lang);
        }

        System.out.println("\n--- LinkedList Demo ---");

        // 2. Tạo một List bằng LinkedList
        List<String> linkedList = new LinkedList<>();
        linkedList.add("HTML");
        linkedList.add("CSS");
        linkedList.add("JavaScript");

        // Thêm vào đầu hoặc cuối LinkedList
        ((LinkedList<String>) linkedList).addFirst("React");
        ((LinkedList<String>) linkedList).addLast("Node.js");

        System.out.println("LinkedList: " + linkedList);

        // Lấy phần tử đầu và cuối
        System.out.println("Phần tử đầu: " + ((LinkedList<String>) linkedList).getFirst());
        System.out.println("Phần tử cuối: " + ((LinkedList<String>) linkedList).getLast());

        // Xóa phần tử đầu và cuối
        ((LinkedList<String>) linkedList).removeFirst();
        ((LinkedList<String>) linkedList).removeLast();
        System.out.println("LinkedList sau khi removeFirst() và removeLast(): " + linkedList);
    }
}
