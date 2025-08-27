package map_interface;

import java.util.*;

public class Cau2 {
    /*
1. HashMap
   - Lưu trữ cặp key-value.
   - Không duy trì thứ tự phần tử.
   - Cho phép 1 null key và nhiều null value.
   - Truy xuất nhanh O(1) trung bình.
   - Thường dùng để tra cứu dữ liệu nhanh.

2. LinkedHashMap
   - Kế thừa từ HashMap.
   - Duy trì thứ tự chèn (insertion-order).
   - Cho phép 1 null key và nhiều null value.
   - Thường dùng khi cần vừa tra cứu nhanh vừa giữ thứ tự thêm vào.

3. TreeMap
   - Cài đặt dựa trên cấu trúc Red-Black Tree.
   - Sắp xếp các phần tử theo thứ tự tự nhiên của key hoặc Comparator tùy chỉnh.
   - Không cho phép null key, nhưng cho phép null value.
   - Thường dùng khi cần dữ liệu được sắp xếp tự động.

4. Hashtable
   - Thread-safe (an toàn đa luồng).
   - Không cho phép null key hoặc null value.
   - Hiệu năng thấp hơn HashMap trong môi trường đơn luồng.
   - Dùng trong các ứng dụng cũ cần đồng bộ.

5. EnumMap
   - Map chuyên dụng cho key kiểu Enum.
   - Rất hiệu quả về bộ nhớ và tốc độ.
   - Các key phải thuộc cùng một enum type.
   - Dùng khi cần map dữ liệu theo Enum (ví dụ: ngày trong tuần, trạng thái).
     */

    public static void main(String[] args) {
        // 1. HashMap - lưu trữ danh sách nhân viên theo ID
        Map<Integer, String> hashMap = new HashMap<>();
        hashMap.put(101, "An");
        hashMap.put(102, "Bình");
        hashMap.put(103, "Chi");
        System.out.println("HashMap (không đảm bảo thứ tự): " + hashMap);

        // 2. LinkedHashMap - lưu trữ và giữ nguyên thứ tự chèn
        Map<Integer, String> linkedHashMap = new LinkedHashMap<>();
        linkedHashMap.put(101, "An");
        linkedHashMap.put(102, "Bình");
        linkedHashMap.put(103, "Chi");
        System.out.println("LinkedHashMap (giữ thứ tự chèn): " + linkedHashMap);

        // 3. TreeMap - lưu trữ có sắp xếp theo key (tự nhiên tăng dần)
        Map<Integer, String> treeMap = new TreeMap<>();
        treeMap.put(103, "Chi");
        treeMap.put(101, "An");
        treeMap.put(102, "Bình");
        System.out.println("TreeMap (sắp xếp theo key): " + treeMap);

        // 4. Hashtable - thread-safe, không cho phép null key/value
        Map<Integer, String> hashtable = new Hashtable<>();
        hashtable.put(101, "An");
        hashtable.put(102, "Bình");
        hashtable.put(103, "Chi");
        System.out.println("Hashtable (an toàn đa luồng): " + hashtable);

        // 5. EnumMap - key là Enum
        enum Day { MONDAY, TUESDAY, WEDNESDAY }
        Map<Day, String> enumMap = new EnumMap<>(Day.class);
        enumMap.put(Day.MONDAY, "Học Java");
        enumMap.put(Day.TUESDAY, "Làm bài tập");
        enumMap.put(Day.WEDNESDAY, "Đi chơi");
        System.out.println("EnumMap (key là Enum): " + enumMap);
    }
}
