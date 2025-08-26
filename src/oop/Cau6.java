package oop;

//private
class Cha{
    private void hienThi(){
        System.out.println("Hiển thị cha");
    }
}

class Con extends Cha{
    public void hienThi(){
        System.out.println("Hiển thị con");
    }
}

//static
class Cha1{
    static void hienThi(){
        System.out.println("Hien thi cha static");
    }
}

class Con1 extends Cha1{
    static void hienThi(){
        System.out.println("Hiển thị con static");
    }
}

public class Cau6 {
    // private
    // một function (method) có access modifier là private hoặc static không thể bị overriding trong Java.

    //Phương thức private
    // Đặc điểm: private chỉ đc dùng trong lớp đó
    // Vì sao không override đươợc:
    //    Override nghĩa là lớp con viết lại phương thức của lớp cha với cùng tên và tham số
    //    Nhưng lớp con không nhìn thấy phương thức private trong lớp cha -> không thể viết lại -> không override được
    //Nếu lớp con viết phương thức cùng tên: Đây không phải override, mà là tạo một phương thức mới hoàn toàn khác



    // static
    // Là phương thức thuộc lớp, không phải thuộc đối tượng
    // Vì sao không ghi đè đc:
    //      Override dựa  vào đối tượng để quyết định phương thức nào được gọi
    //      static không dựa vào đối tượng mà dựa vào lớp -> không thể override
    // Nê lớp con viết phương thức static cùng tên: Đây không phải override, mà là ẩn  phương ức của lớp cha
    public static void main(String[] args) {
        Con c = new Con();
        c.hienThi();

        Cha1 p = new Con1();
        p.hienThi();
        Con1.hienThi();

    }
}
