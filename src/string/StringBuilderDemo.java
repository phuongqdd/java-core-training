package string;

public class StringBuilderDemo {
    public static void main(String[] args) {
        StringBuilder log = new StringBuilder("Log start\n");

        // Thêm các sự kiện
        log.append("User login\n");
        log.append("User clicked button\n");
        log.append("User logout\n");

        // Chèn sự kiện ở đầu log
        log.insert(0, "Timestamp: 2025-08-26 10:00\n");

        // Xóa dòng không cần thiết
        log.delete(log.indexOf("User clicked button"), log.indexOf("User clicked button") + "User clicked button\n".length());

        System.out.println(log.toString());
    }
}
