package primitive_and_object;

public class ChuyenDoiDuLieu {
    public static void main(String[] args) {
        // ------------------------
        // 1. Kiểu dữ liệu nguyên thủy
        int x = 10;
        System.out.println("Giá trị int (primitive): " + x);

        // ------------------------
        // 2. Boxing (chuyển primitive -> object)
        Integer obj1 = Integer.valueOf(x);   // Cách thủ công
        Integer obj2 = x;                   // Autoboxing (Java tự làm)
        System.out.println("Integer object (Boxing thủ công): " + obj1);
        System.out.println("Integer object (Autoboxing): " + obj2);

        // ------------------------
        // 3. Unboxing (chuyển object -> primitive)
        int y1 = obj1.intValue();   // Cách thủ công
        int y2 = obj2;              // Auto-unboxing (Java tự làm)
        System.out.println("int từ object (Unboxing thủ công): " + y1);
        System.out.println("int từ object (Auto-unboxing): " + y2);

        // ------------------------
        // 4. Demo dùng trong phép toán
        Integer a = 5;              // Autoboxing từ int -> Integer
        int b = a + 15;             // Auto-unboxing từ Integer -> int rồi cộng
        System.out.println("Kết quả phép toán: " + b);
    }
}
