package set_interface;

import java.util.*;
import java.util.concurrent.CopyOnWriteArraySet;

public class Cau3 {

    /*

| Class                  | Đặc điểm chính                                         | Trường hợp sử dụng điển hình                    |
|-------------------------|-------------------------------------------------------|-------------------------------------------------|
| HashSet                 | - Dựa trên hash table, không bảo đảm thứ tự           | - Lọc dữ liệu trùng lặp                         |
|                         | - Thao tác cơ bản O(1)                                | - Kiểm tra tồn tại nhanh                        |
|                         | - Cho phép 1 phần tử null                             | - Danh sách ID, email, từ khóa                  |
|-------------------------|-------------------------------------------------------|-------------------------------------------------|
| LinkedHashSet           | - Kế thừa HashSet                                     | - Khi cần giữ thứ tự thêm vào                   |
|                         | - Duy trì thứ tự thêm vào (insertion order)           | - Menu, danh sách từ khóa theo thứ tự           |
|-------------------------|-------------------------------------------------------|-------------------------------------------------|
| TreeSet                 | - Dựa trên Red-Black Tree                             | - Danh sách phần tử duy nhất sắp xếp tự động    |
|                         | - Tự động sắp xếp theo natural order hoặc Comparator  | - Bảng xếp hạng, leaderboard                    |
|                         | - O(log n)                                            | - Danh sách số thứ tự                           |
|-------------------------|-------------------------------------------------------|-------------------------------------------------|
| EnumSet                 | - Chỉ dùng với enum                                   | - Tập hợp enum: ngày trong tuần, trạng thái     |
|                         | - Hiệu năng cao, tiết kiệm bộ nhớ                     | - Trạng thái hệ thống                           |
|-------------------------|-------------------------------------------------------|-------------------------------------------------|
| CopyOnWriteArraySet     | - Thread-safe, dựa trên CopyOnWriteArrayList          | - Danh sách người online, session               |
|                         | - Thích hợp nhiều thread đọc, ít ghi                  | - Dùng trong môi trường đa luồng                |
==========================================================
*/
    // Enum cho lịch học
    enum Day { MON, TUE, WED, THU, FRI, SAT, SUN }

    public static void main(String[] args) {
        // 1. HashSet - Danh sách sinh viên (không trùng lặp, không quan tâm thứ tự)
        Set<String> students = new HashSet<>();
        students.add("An");
        students.add("Bình");
        students.add("Chi");
        students.add("An"); // bị loại bỏ
        System.out.println("Danh sách sinh viên (HashSet): " + students);

        // 2. LinkedHashSet - Giữ thứ tự đăng ký môn học
        Set<String> courses = new LinkedHashSet<>();
        courses.add("Toán");
        courses.add("Lý");
        courses.add("Hóa");
        System.out.println("Thứ tự đăng ký môn học (LinkedHashSet): " + courses);

        // 3. TreeSet - Bảng điểm thi (tự động sắp xếp tăng dần)
        Set<Integer> scores = new TreeSet<>();
        scores.add(85);
        scores.add(70);
        scores.add(92);
        scores.add(70); // trùng, bị bỏ qua
        System.out.println("Điểm thi đã sắp xếp (TreeSet): " + scores);

        // 4. EnumSet - Lịch học trong tuần
        EnumSet<Day> schedule = EnumSet.of(Day.MON, Day.WED, Day.FRI);
        System.out.println("Lịch học (EnumSet): " + schedule);

        // 5. CopyOnWriteArraySet - Danh sách sinh viên đang online trong lớp học trực tuyến
        Set<String> onlineStudents = new CopyOnWriteArraySet<>();
        onlineStudents.add("An");
        onlineStudents.add("Bình");
        onlineStudents.add("Bình"); // trùng, bỏ qua
        System.out.println("Sinh viên đang online (CopyOnWriteArraySet): " + onlineStudents);
    }
}
