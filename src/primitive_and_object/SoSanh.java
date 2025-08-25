package primitive_and_object;

public class SoSanh {
    public static void main(String[] args){
        // Dùng ==: Java sẽ unbox (giải nén) object về primitive, sau đó mới so sánh giá trị.
        Integer a = Integer.valueOf(100);  // lấy từ cache
        Integer b = Integer.valueOf(100);  // cũng từ cache
        Integer c = Integer.valueOf(200);  // ngoài cache
        Integer d = Integer.valueOf(200);  // new object

        System.out.println(a == b); // true (cùng object trong cache)
        System.out.println(c == d); // false (khác object)
        System.out.println(c.equals(d)); // true (giá trị giống nhau)

        String s1 = "Phuong";
        String s2 = "Phuong";
        System.out.println(s1 == s2);

        String s3 = new String("Phuong");
        String s4 = new String("Phuong");
        System.out.println(s3 == s4);

        // Dùng b.equals(a): a (primitive) được autobox thành Integer. Sau đó .equals() so sánh giá trị bên trong object.
//        Điểm khác biệt quan trọng
//        ==:
//          Nếu có primitive tham gia → Java sẽ unbox object về primitive, sau đó so sánh giá trị.
//        .equals():
//          Luôn so sánh giá trị bên trong object.
//          Nếu tham số là primitive → nó được autobox thành object trước.

        int p = 200;
        Integer o = 200;

        System.out.println("p == o : " + (p == o));       // true (unboxing)
        System.out.println("o.equals(p) : " + o.equals(p)); // true (autoboxing)

        System.out.println(s3.equals(s4));
    }
}
