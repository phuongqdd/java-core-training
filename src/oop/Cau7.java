package oop;

class Cha2{
    void hienThi(){
        System.out.println("Cha: Hien thi ma dinh");
    }
}

class Con2 extends Cha2{
    @Override
    void hienThi(){
        System.out.println("COn: Hiển thị đã được ghi đè");
    }

    void hienThi(String message){
        System.out.println("Con: " + message);
    }

    void hienThi(String message, int count){
        for(int i = 0; i < count; i++){
            System.out.println(i + " - Con (overload): " + message);
        }
    }

    int hienThi(int x){
        return x + 17;
    }
}

public class Cau7 {
    // Overriding (Ghi đè)
    //  Xảy ra khi lớp con viết lại phương thức giống hệt lớp cha:
    //          Cùng tên.
    //          Cùng tham số (parameter list).
    //          Kiểu trả về giống hoặc là kiểu con (covariant return type).
    //  Access modifier không được giảm (có thể tăng).
    //  Thể hiện đa hình (polymorphism) → Khi chạy, phương thức được gọi tùy thuộc đối tượng thực tế.

    // Overloading (Nạp chồng)
    // Xảy ra trong cùng 1 class (hoặc class con/cha)
    // CÙng tên nhuưng khác danh sách tham số (số lượng, kiểu hoặc  thứ tự)
    // Kiểu trả về có thể khác nhưng không dùng để phân biệt overload
    // Xay ra o compile-time

    public static void main(String[] args) {
        Cha2 con = new Con2();
        con.hienThi();

        Con2 con1 = new Con2();
        con1.hienThi("Hi hi");
        con1.hienThi("Hi hi", 3);
        System.out.println(con1.hienThi(4));


    }

}
