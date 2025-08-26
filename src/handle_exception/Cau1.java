package handle_exception;

public class Cau1 {

    // => throw = ném ra lỗi, throws = báo trước method có thể ném lỗi.

    public static int divide(int a, int b) throws ArithmeticException{
        return a / b;
    }

    public static void main(String[] args) {
        //throw
        // DÙng để ném ra (throw) một exception cụ thể trong code
        // Chỉ ném một đối tượng (Object) exception tại một thời điểm
        // Vị trí dùng: bên trong thân hàm
        // Sau throw luôn là một đối tượng
        // chủ yếu được sử dụng để ném ngoại lệ tùy chỉnh (ngoại lệ do người dùng tự định nghĩa).

        int age = 18;
        if(age < 18){
            throw new ArithmeticException("tuổi phải >= 18");
        }
        System.out.println("Được phép đăng ký!");


        //throws
        //Dùng để khai báo rằng 1 method có thể ném ra exception
        // Có thể khai báo nhiều exception cùng lúc (Phân cách bằng dấu phẩu);
        // Vị trí dùng: sau tên hàm, trong phần khai báo method
        // Không trực tiếp ném exception, mà chỉ bo trước cho caller biết để caller xử lý bằng try-catch hoặc tiếp tục khai báo throws

        try{
            int result = divide(10, 2);
            System.out.println("Kết quả: " + result);

            result = divide(10, 0);
            System.out.println("Kết quả: " + result);
        }catch (ArithmeticException e){
            System.out.println("Lỗi: " + e.getMessage());
        }

    }
}
