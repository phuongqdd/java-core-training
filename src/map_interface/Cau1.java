package map_interface;

import java.util.*;

public class Cau1 {
    // Lưu trữ dưới dạng key-value
    // Key là duy nhất
    //      Một key chỉ có thể ánh xạ đến một value
    //      Nếu thêm một key chỉ có thể ánh xạ đến một value
    // Value có thể trùng lặp
    //      Không giới hạn, nhiều key có thể tr đến cùng một value
    // Không kế thừa từ Collection
    //      Map là một interface độc lập, không mở rộng từ Collection
    // Duy trì 3 tập hợp khác nhau
    //      Tập hợp các key
    //      Tập hợp các value
    //      Mapping: tập hợp các key/value (liên kết key/value).
    // Sử dụng key và value là null: Một số cài đặt Map cho phép sử dụng key và value là null.
    //  Trong đó, HashMap và LinkedHashMap cho phép bạn sử dụng cả key và value là null.
    //  TreeMap không cho phép sử dụng key là null.
    // Không đảm bảo thứ tự (tùy implement)
    //      HashMap: không có thứ tự.
    //      LinkedHashMap: duy trì thứ tự chèn.
    //      TreeMap: sắp xếp theo thứ tự tự nhiên hoặc comparator.
    // Map dùng hashCode() để tìm bucket, equals() để so sánh key trong bucket.
    //      Nếu key thay đổi sau khi được đưa vào Map (làm đổi hashCode hoặc equals), thì việc tìm kiếm sẽ thất bại → trả về null.
    //      Vì vậy, key nên là immutable (bất biến), ví dụ String, Integer, UUID, …
    //      => Tóm gọn 1 câu: Key trong Map nên bất biến, nếu thay đổi sẽ không tìm lại được entry.

    public static void main(String[] args) {
        // HashMap: không đảm bảo thứ tự
        Map<Integer, String> studentMap = new HashMap<>();

        studentMap.put(101, "Phương");
        studentMap.put(102, "Phúc");
        studentMap.put(103, "An");
        studentMap.put(101, "Quang"); // Ghi đè key = 101

        System.out.println("Danh sách sinh viên:");
        for (Map.Entry<Integer, String> entry : studentMap.entrySet()) {
            System.out.println("ID: " + entry.getKey() + " - Tên: " + entry.getValue());
        }

        // LinkedHashMap: duy trì thứ tự chèn
        Map<String, Integer> cart = new LinkedHashMap<>();

        cart.put("iPhone", 2);
        cart.put("MacBook", 1);
        cart.put("AirPods", 3);

        System.out.println("Giỏ hàng theo thứ tự thêm:");
        for (Map.Entry<String, Integer> entry : cart.entrySet()) {
            System.out.println(entry.getKey() + " - Số lượng: " + entry.getValue());
        }

        // TreeMap: tự động sắp xếp theo key (tăng dần)
        Map<String, String> contacts = new TreeMap<>();

        contacts.put("Zara", "0909999999");
        contacts.put("Anna", "0901234567");
        contacts.put("Tom", "0908888888");

        System.out.println("Danh bạ (sắp xếp theo tên):");
        for (Map.Entry<String, String> entry : contacts.entrySet()) {
            System.out.println("Tên: " + entry.getKey() + " - SĐT: " + entry.getValue());
        }

        // Hashtable: thread-safe (an toàn đa luồng), không cho null key/value
        Map<String, String> accounts = new Hashtable<>();

        accounts.put("admin", "123456");
        accounts.put("user", "abcxyz");

        System.out.println("Danh sách tài khoản:");
        for (Map.Entry<String, String> entry : accounts.entrySet()) {
            System.out.println("Username: " + entry.getKey() + " - Password: " + entry.getValue());
        }

    }
}
