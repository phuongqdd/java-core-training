package handle_exception;

class AgeNotValueException extends Exception{
    public AgeNotValueException(String message){
        super(message);
    }
}

public class Cau4 {
    // Chọn loại exception
    //      Nếu muôn checked exception -> extends Exception
    //      Nếu muốn unchecked exception -> extends RuntimeException
    //Tạo Contructor
    //      Constructor có thể nhận message để thể hiện thông tin lỗi

    public static void checkAge(int age) throws AgeNotValueException{
        if(age < 18){
            throw new AgeNotValueException("Tuooir phải >= 18");
        }
        System.out.println("tuổi hợp lẹ: " + age);
    }

    public static void main(String[] args) {
        try {
            checkAge(18);
        } catch (AgeNotValueException e) {
            System.out.println("Lỗi: " + e.getMessage());
        }
    }
}
