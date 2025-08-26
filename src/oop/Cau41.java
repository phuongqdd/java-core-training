package oop;


abstract class DongVat2{
    String ten;
    DongVat2(String ten){
        this.ten = ten;
    }

    void an(){
        System.out.println(ten + " đang ăn");
    }

    abstract void keu();
}

class Cho1 extends DongVat2{
    Cho1(String ten){
        super(ten);
    }

    @Override
    void keu(){
        System.out.println(ten + " sửa: Gau Gau");
    }
}

class Meo1 extends DongVat2 {
    Meo1(String ten) {
        super(ten);
    }

    @Override
    void keu() {
        System.out.println(ten + " kêu: Meo meo!");
    }
}

// INTERFACE
interface BayDuoc {
    void bay();
}

interface BoiDuoc {
    void boi();
}

class Vit1 extends DongVat2 implements BayDuoc, BoiDuoc {
    Vit1(String ten) {
        super(ten);
    }

    @Override
    void keu() {
        System.out.println(ten + " kêu: Quạc quạc!");
    }

    @Override
    public void bay() {
        System.out.println(ten + " bay là là mặt nước.");
    }

    @Override
    public void boi() {
        System.out.println(ten + " bơi dưới ao.");
    }
}

public class Cau41 {
    // Abstract
    //   Một class chỉ kế thừa được 1 abstract class
    //   Chứa biến instance, constructor, phương thức có code, phương thức abstract
    //   Cho phép trộn cả code đã triển khai và chưa triển khai
    //   Khi có quan hệ kế thừa và cần chia sẻ code chung

    //Interface
    // Một class có thể implement nhiều interface
    // Chỉ chứa hằng số và phương thức abstract. Từ java8 có default method và static method
    // Hoàn toàn là bản thiê kế thường không có code triển khai (trừ default method)
    // Khi câần định nghĩa hành vi cho nhiều class không liên quan hoặc cần đa kế thừa hành vi

    // Abstract : dùng khi muốn tạo một class cha có code chung cho các class con
    // Interface: DÙng khi bạn cần ràng buộc hành vi cho các class khác nhau, không cùng hệ thống kế thừa

    public static void main(String[] args) {
        DongVat2 cho = new Cho1("Cún");
        cho.an();
        cho.keu();

        DongVat2 meo = new Meo1("Mướp");
        meo.an();
        meo.keu();

        Vit1 vit = new Vit1("Vịt trời");
        vit.an();
        vit.keu();
        vit.bay();
        vit.boi();
    }

}
