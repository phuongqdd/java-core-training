package oop;
class Cha3{
    final void hienThi(){
        System.out.println("Cha: final mehtod");
    }
}

class Con3 extends Cha3{
    // Không được phép override
//    void hienThi(){
//        System.out.println("Con: muon overrif;");
//    }
}

public class Cau8 {

    // 1 phương thức final -> không thể override trong lớp con
    // Lớp con vẫn kế thừa phương thức final từ lớp cha
    // Khi gọi, nó sẽ luôn chạy phiên bản của lớp cha

    public static void main(String[] args) {
        Con3 c = new Con3();
        c.hienThi();  //In: "Cha: final method"
    }
}
