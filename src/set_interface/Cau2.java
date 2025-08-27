package set_interface;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Cau2 {
    public static void main(String[] args) {
        // Đặc điểm:
        //      Dựa trên hash table, không bảo đảm thứ tự.
        //      Thao tác cơ bản (add, remove, contains) ~ O(1).
        //      Cho phép 1 phần tử null.
        //  Trường hợp sử dụng:
        //      Loại bỏ phần tử trùng lặp, lọc dữ liệu, kiểm tra tồn tại nhanh.

        System.out.println("=== 1. HashSet: nhanh, loại bỏ trùng lặp ===");
        Set<String> hashSet = new HashSet<>();
        hashSet.add("Java");
        hashSet.add("Python");
        hashSet.add("C++");
        hashSet.add("Java"); // trùng, không thêm
        hashSet.add(null);   // cho phép null
        System.out.println("HashSet: " + hashSet);
        System.out.println("Ứng dụng: lọc dữ liệu trùng lặp, kiểm tra tồn tại");

        //  LinkedHashSet
        //  Đặc điểm:
        //      Kế thừa từ HashSet, nhưng duy trì thứ tự thêm vào (insertion order).
        //      Thao tác add/remove có chi phí ~ O(1).
        //  Trường hợp sử dụng:
        //      Khi cần danh sách không trùng lặp nhưng giữ thứ tự thêm, ví dụ: danh sách menu, các từ khóa theo thứ tự nhập.

        System.out.println("\n=== 2. LinkedHashSet: duy trì thứ tự thêm ===");
        Set<String> linkedHashSet = new LinkedHashSet<>();
        linkedHashSet.add("Apple");
        linkedHashSet.add("Banana");
        linkedHashSet.add("Cherry");
        linkedHashSet.add("Apple"); // trùng, không thêm
        System.out.println("LinkedHashSet: " + linkedHashSet);
        System.out.println("Ứng dụng: danh sách không trùng lặp nhưng giữ thứ tự");

        //  TreeSet
        //  Đặc điểm:
        //      Dựa trên cây đỏ-đen (Red-Black Tree).
        //      Tự động sắp xếp theo natural order hoặc Comparator.
        //      Không cho phép null khi dùng natural ordering.
        //      Thao tác cơ bản ~ O(log n).
        //  Trường hợp sử dụng:
        //      Khi cần danh sách phần tử duy nhất và sắp xếp, ví dụ: bảng xếp hạng, danh sách số thứ tự.

        System.out.println("\n=== 3. TreeSet: sắp xếp tự động ===");
        Set<Integer> treeSet = new TreeSet<>();
        treeSet.add(50);
        treeSet.add(10);
        treeSet.add(30);
        treeSet.add(20);
        System.out.println("TreeSet (sắp xếp): " + treeSet);
        System.out.println("Ứng dụng: danh sách duy nhất và sắp xếp tự động");

        // EnumSet
        //  Đặc điểm:
        //      Chỉ dùng với enum.
        //      Hiệu năng cao, tiết kiệm bộ nhớ.
        //      Bắt buộc tất cả phần tử phải là enum cùng loại.
        //  Trường hợp sử dụng:
        //      Khi làm việc với tập hợp enum, ví dụ: các ngày trong tuần, trạng thái hệ thống.

        System.out.println("\n=== 4. EnumSet: tối ưu cho enum ===");
        enum Day { MON, TUE, WED, THU, FRI , CONDIEN}
        Set<Day> workingDays = EnumSet.of(Day.WED, Day.FRI, Day.MON, Day.TUE, Day.WED, Day.CONDIEN,Day.THU, Day.FRI);
        System.out.println("Working days: " + workingDays);
        System.out.println("Ứng dụng: tập hợp enum, ví dụ các ngày trong tuần");

        // CopyOnWriteArraySet
        // Đặc điểm:
        //      Thread-safe, dựa trên CopyOnWriteArrayList.
        //      Thích hợp nhiều thread đọc, ít ghi.
        //      Mỗi khi thêm/xóa, tạo bản sao mới của mảng.
        //  Trường hợp sử dụng:
        //      Danh sách người online, session, cấu hình dùng nhiều thread đọc.

        System.out.println("\n=== 5. CopyOnWriteArraySet: thread-safe, đọc nhiều ghi ít ===");
        Set<String> cowSet = new CopyOnWriteArraySet<>();
        cowSet.add("Alice");
        cowSet.add("Bob");
        cowSet.add("Charlie");
        cowSet.add("Alice"); // trùng, không thêm
        System.out.println("CopyOnWriteArraySet: " + cowSet);
        System.out.println("Ứng dụng: danh sách người online, session, thread-safe");
    }
}
