package oop;

// 2 interface
interface A{
    void hienThi();
}

interface B{
    void hienThi();
}

class C implements A, B{

    @Override
    public void hienThi() {
        System.out.println("Lop c");
    }
}

interface A1 {
    int hienThi();
}

interface B1 {
    String hienThi();
}

//class C1 implements A1, B1 {
//    @Override
//    public int hienThi() {
//        return 0;
//    }
//    // Lỗi: không thể định nghĩa cả 2 vì xung đột kiểu trả về
//}


abstract class A2{
    abstract void hienThi();
}

interface B2{
    void hienThi();
}

class C2 extends A2 implements B2{

    @Override
    public void hienThi() {
        System.out.println("Hi hi 1 no 1 kia");
    }
}

public class Cau4 {
    // Hai interface có cùng tên hàm, cùng kiểu trả về
    //      Không có vấn đề gì
    //      Class implement cả 2 interface thì chỉ cần viết lại (override) 1 lần.
    //      Vì cùng tên, cùng tham số, cùng kiểu trả về → Java coi như một hàm duy nhất.

    // Hai interface có cùng tên hàm nhưng khác kiểu trả về
    //      Bị lỗi biên dịch
    //      Java không cho phép 1 class implement 2 interface có cùng tên, cùng tham số mà khác kiểu trả về -> không biết chọn cái nào


    // Một abstract class và 1 interface có cùng tên hàm, cunùng kiểu trả về
    //       Không có vấn đề gì
    //        Vì abstract class đã khai báo (chưa triển khai), interface cũng yêu cầu khai báo -> Class con viết lại là đủ

    public static void main(String[] args) {
        C c = new C();
        c.hienThi();

        C2 c2 = new C2();
        c2.hienThi();
    }

}
