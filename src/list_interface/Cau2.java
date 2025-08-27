package list_interface;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

public class Cau2 {
    //Các class triển khai từ Listinterface
    //  ArrayLisst: Dựa trên mảng, truy xuất theo index nhanh, thêm/xóa cuối list nhanh, không thread-safe
    //  LinkedList: Dựa trên liên kết đôi, thêm/xóa đâ hoẵ giữa nhanh, truy xuất index chậm hơn ArrayList
    //  Vector: Dựa trên mảng, thread-safe, ít dùng
    //  Stack: Mở rôộng từ Vector, hỗ trợ thao tác kiểu LIFO,, dùng làm stack
    //  CopyOnWriteArrayList: Thread-safe, sao chép mảng khi thay đổi, dùng trong môi trường đa luồng

    // Ghi nhớ:
    //      ArrayList: nhanh truy xuất, chậm thêm/xóa đầu hoặc giữa
    //      LinkedList: nhanh thêm/xóa đầu hoặc giữa, chậm truy xuất index.
    //      Vector & Stack: thread-safe, Stack dùng LIFO.
    //      CopyOnWriteArrayList: thread-safe, thích hợp môi trường nhiều thread đọc, ít ghi.

    public static void main(String[] args) {
        List<String> arrayList = new ArrayList<>();
        arrayList.add("Java");
        arrayList.add("Python");
        arrayList.add("C++");
        System.out.println("ArrayList: " + arrayList);

        List<String> linkedList = new LinkedList<>();
        linkedList.add("HTML");
        linkedList.add("CSS");
        linkedList.add("JavaScript");
        System.out.println("LinkedList: " + linkedList);

        List<String> vector = new Vector<>();
        vector.add("Red");
        vector.add("Green");
        vector.add("Blue");
        System.out.println("Vector: " + vector);

        Stack<String> stack = new Stack<>();
        stack.push("Apple");
        stack.push("Banana");
        stack.push("Cherry");
        System.out.println("Stack (LIFO): " + stack);
        System.out.println("Pop từ Stack: " + stack.pop());
        System.out.println("Stack sau pop: " + stack);

        List<String> cowList = new CopyOnWriteArrayList<>();
        cowList.add("One");
        cowList.add("Two");
        cowList.add("Three");
        System.out.println("CopyOnWriteArrayList: " + cowList);
    }
}
