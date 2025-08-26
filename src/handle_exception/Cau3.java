package handle_exception;

import java.io.FileReader;
import java.io.IOException;

public class Cau3 {
    public static void main(String[] args) {
        // try-catch thông thường
        // Dùng để bắt và xử lý ngoại lệ
        // Cần tự tay đóng tài nguyên

        FileReader fr = null;
        try {
            fr = new FileReader("abc.txt");
            int data = fr.read();
            System.out.println("Đọc dữ liệu: " + data);
        } catch (IOException e) {
            System.out.println("Lỗi: " + e.getMessage());
        } finally {
            try {
                if (fr != null) fr.close(); // phải tự đóng
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        //try-catch-resources
        // dùng để tự động quản lý tài nguyên(auto-close) khi xong, dù có exception hay không
        // chỉ áp dụng cho tài nguyên implement interface AutoCloseable (FileReader, BufferedReader)
        try (FileReader fr1 = new FileReader("abc.txt")) {
            int data = fr1.read();
            System.out.println("Đọc dữ liệu: " + data);
        } catch (IOException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}
