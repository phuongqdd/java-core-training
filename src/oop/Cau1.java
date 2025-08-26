package oop;


//Dong goi
class HocSinh1 {
    private String ten;  // biến private
    private int tuoi;

    // constructor
    public HocSinh1(String ten, int tuoi) {
        this.ten = ten;
        this.tuoi = tuoi;
    }

    // getter & setter
    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public int getTuoi() {
        return tuoi;
    }

    public void setTuoi(int tuoi) {
        if (tuoi > 0) this.tuoi = tuoi;
    }
}

//Ke thua
class Nguoi {
    String ten;

    public Nguoi(String ten) {
        this.ten = ten;
    }

    void gioiThieu() {
        System.out.println("Tôi tên là " + ten);
    }
}

class GiaoVien extends Nguoi {
    String monDay;

    public GiaoVien(String ten, String monDay) {
        super(ten); // gọi constructor của cha
        this.monDay = monDay;
    }

    void dayHoc() {
        System.out.println(ten + " đang dạy môn " + monDay);
    }
}

// Đa hình
class DongVat {
    void keu() {
        System.out.println("Động vật kêu...");
    }
}

class Cho extends DongVat {
    @Override
    void keu() {
        System.out.println("Gâu gâu");
    }
}

class Meo extends DongVat {
    @Override
    void keu() {
        System.out.println("Meo meo");
    }
}

// abstract class
abstract class DongVat1 {
    abstract void keu(); // phương thức trừu tượng
}

class Heo extends DongVat1 {
    @Override
    void keu() {
        System.out.println("Ụt ịt");
    }
}

class Vit extends DongVat1 {
    @Override
    void keu() {
        System.out.println("Quạc quạc");
    }
}



public class Cau1 {
    public static void main(String[] args) {
        HocSinh1 hs = new HocSinh1("Phương", 22);
        hs.setTuoi(23); // cập nhật tuổi an toàn
        System.out.println(hs.getTen() + " - " + hs.getTuoi());

        GiaoVien gv = new GiaoVien("Thầy Nam", "Toán");
        gv.gioiThieu();
        gv.dayHoc();

        DongVat dv1 = new Cho();
        DongVat dv2 = new Meo();
        dv1.keu();
        dv2.keu();

        DongVat1 dv = new Heo();
        dv.keu();
    }
}
