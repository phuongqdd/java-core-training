package oop;

class HocSinh{
    String ten;
    int tuoi;

    HocSinh(String ten, int tuoi){
        this.ten = ten;
        this.tuoi = tuoi;
    }

    void hienThi(){
        System.out.println("Tên: " + this.ten + ", Tuổi: " + this.tuoi);
    }
}

public class Cau9 {

    public static void main(String[] args) {
        // This
        // Đại diện cho chính object hiện tại
        HocSinh hs = new HocSinh("Phương", 22);
        hs.hienThi();
    }
}
