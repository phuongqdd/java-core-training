package set_interface;

import java.util.HashSet;
import java.util.Set;

public class Cau1 {
    // Set interface mở rộng từ collection interface
    // Đại diện cho tập hợp các phần tử, không chứa phần tử trùng lặp

    // Đặc điểm
    // 1. Không chứa phần tử trùng lặp
    // 2. Không đảm bảo thứ tự
    //      HashSet: không đảm bảo thứ tự
    //      LinkedHashSet: duy trì thứ tự thêm vào
    //      TreeSet: Sắp xep tu dong theo natural order hoac comparator
    //  3. Không index-based access
    //      Khômg thể truy xuất phần tử theo chỉ số như List
    //      Phải duyệt qua iterator hoặc for-each
    //  4. Cho phép null(tu implement)
    //      HashSet, LinkedHashSet: cho phép 1 phần tử null.
    //      TreeSet: không cho phép null nếu dùng natural ordering, có thể dùng comparator cho phép null.
    //  5. Hiệu năng
    //      HashSet: thao tác cơ bản (add, remove, contains) ~ O(1).
    //      LinkedHashSet: O(1) nhưng thêm bộ nhớ để lưu thứ tự.
    //      TreeSet: O(log n) do sử dụng cây đỏ-đen (Red-Black Tree).
    public static void main(String[] args) {
        // Create a hash set
        Set<String> set = new HashSet<>();

        // Add strings to the set
        set.add("London");
        set.add("Paris");
        set.add("New York");
        set.add("San Francisco");
        set.add("Beijing");
        set.add("New York");

        System.out.println(set);
        // Display the elements in the hash set
        for (String s: set) {
            System.out.print(s.toUpperCase() + " ");
        }
    }
}
