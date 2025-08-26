package handle_exception;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

public class Cau2 {
    public static void main(String[] args) {
        // Checked exception mà Java buộc phải xử lý tại thời điểm compile-time
        // Nếu bạn gọi method có checked exception mà không xử lý bằng try-catch hoặc không khai báo throws, compile-time error xảy ra
        // Thường liên quan đến I/O, database, networking

        try{
            File file = new File("abc1.txt");
            FileReader fileReader = new FileReader(file);
        } catch (FileNotFoundException e) {
            System.out.println("File không tồn tại: " + e.getMessage());
        }

        // Unchecked Exception mà Java không bắt buộc phải xử lý lúc compile-time
        // Xuất hiện kho có lỗi logic hoặc runtime

        int a = 10, b = 0;
        System.out.println(a / b);
    }
}
