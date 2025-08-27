package map_interface;

import java.util.*;

public class Cau3 {
   /*
   1. HashMap
- Lưu dữ liệu dưới dạng key-value.
- Không đảm bảo thứ tự key.
- Cho phép 1 null key, nhiều null value.
- Nhanh nhất trong hầu hết trường hợp (O(1) lookup).
=> Khi cần truy cập nhanh bằng key và không quan trọng thứ tự.
Ví dụ: Lưu thông tin nhân viên theo ID (ID → Tên).

2. LinkedHashMap
- Giữ thứ tự chèn hoặc thứ tự truy cập.
- Cho phép 1 null key, nhiều null value.
- Hiệu năng chậm hơn HashMap chút ít.
=> Khi cần vừa truy cập nhanh, vừa duy trì thứ tự (in ra theo đúng thứ tự thêm vào hoặc truy cập gần nhất).
Ví dụ: Cache dữ liệu (LRU Cache), lưu log theo thứ tự thao tác.

3. TreeMap
- Các key được sắp xếp tự động (theo thứ tự tự nhiên hoặc Comparator).
- Không cho phép null key.
- Truy xuất O(log n).
=> Khi cần Map có thứ tự sắp xếp theo key.
Ví dụ: Sắp xếp sinh viên theo điểm, danh bạ điện thoại theo tên.

4. Hashtable
- Thread-safe (đồng bộ hóa).
- Không cho phép null key hoặc null value.
- Hiệu năng thấp hơn HashMap vì lock toàn bộ bảng.
=> Khi cần Map hoạt động trong đa luồng cũ (trước khi có ConcurrentHashMap).
Ví dụ: Ứng dụng legacy, lưu session người dùng trong server multi-thread.

5. EnumMap
- Key chỉ có thể là Enum.
- Rất hiệu quả về bộ nhớ và tốc độ.
- Các key Enum được lưu trữ theo thứ tự định nghĩa trong Enum.
=> Khi key là Enum, thay vì dùng HashMap để tiết kiệm bộ nhớ.
Ví dụ: Lịch làm việc (DayOfWeek → Công việc).
    */
    enum Day { MONDAY, TUESDAY, WEDNESDAY, THURSDAY, FRIDAY }
    public static void main(String[] args) {
        HashMap<Integer, String> employees = new HashMap<>();
        employees.put(101, "Nguyen Van A");
        employees.put(102, "Tran Thi B");
        employees.put(103, "Le Van C");

        // Tìm kiếm nhanh theo ID
        System.out.println("Nhân viên ID 102: " + employees.get(102));

        // LRU Cache với capacity = 3
        LinkedHashMap<Integer, String> cache = new LinkedHashMap<>(3, 0.75f, true) {
            protected boolean removeEldestEntry(Map.Entry eldest) {
                return size() > 3;
            }
        };

        cache.put(1, "A");
        cache.put(2, "B");
        cache.put(3, "C");
        cache.get(1); //
        cache.get(2);// truy cập A
        cache.put(4, "D"); // Xóa phần tử lâu nhất không dùng (2, "B")

        System.out.println("Cache hiện tại: " + cache);

        TreeMap<String, String> phoneBook = new TreeMap<>();
        phoneBook.put("An", "0123456789");
        phoneBook.put("Binh", "0987654321");
        phoneBook.put("Cuong", "0909090909");

        System.out.println("Danh bạ: " + phoneBook);

        Hashtable<String, String> sessions = new Hashtable<>();
        sessions.put("user1", "session1");
        sessions.put("user2", "session2");

        // Giả lập tìm session
        System.out.println("Session của user1: " + sessions.get("user1"));

        EnumMap<Day, String> schedule = new EnumMap<>(Day.class);
        schedule.put(Day.MONDAY, "Họp nhóm");
        schedule.put(Day.TUESDAY, "Viết báo cáo");
        schedule.put(Day.WEDNESDAY, "Code dự án");
        schedule.put(Day.THURSDAY, "Thuyết trình");
        schedule.put(Day.FRIDAY, "Review sản phẩm");

        System.out.println("Lịch thứ Tư: " + schedule.get(Day.WEDNESDAY));
    }
}
