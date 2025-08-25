package static_final;

//Bien static
class Counter {
    static int count = 0; // biến static (biến tĩnh)

    Counter() {
        count++; // mỗi lần tạo object thì count tăng
    }
}

//static method
class MathUtil {
    static int add(int a, int b) {
        return a + b;
    }
}

//static blocks
class StaticBlock {
    static int x;

    // khối static
    static {
        System.out.println("Khối static được chạy");
        x = 10; // khởi tạo biến static
    }
}

//Static Class
class Outer {
    int a = 5;
    static int b = 10;

    // nested static class
    static class Inner {
        void show() {
//             System.out.println(a); Lỗi: không truy cập được biến non-static
            System.out.println("Giá trị b = " + b); // OK
        }
    }
}

class User {
    String name;
    int age;

    // nested static class
    static class Address {
        String city;
        String country;

        Address(String city, String country) {
            this.city = city;
            this.country = country;
        }

        void show() {
            System.out.println(city + ", " + country);
        }
    }
}

// Dùng thuộc tính static
class Student {
    String name;
    static String school = "Trường Đại học Sư Phạm Hà Nội";  // tất cả sinh viên đều chung trường

    Student(String name) {
        this.name = name;
    }

    void showInfo() {
        System.out.println(name + " học tại " + school);
    }
}

//Dùng khối static
class Config {
    static String dbUrl;

    static {
        // Khởi tạo biến static
        dbUrl = "jdbc:mysql://localhost:3306/test";
        System.out.println("Static block đã chạy!");
    }
}



public class Static {
    public static void main(String[] args) {
        Counter c1 = new Counter();
        Counter c2 = new Counter();
        Counter c3 = new Counter();

        System.out.println("Số lượng object = " + Counter.count); // 3

        System.out.println(MathUtil.add(5, 10)); // gọi trực tiếp

        System.out.println("Giá trị x = " + StaticBlock.x);

        Outer.Inner obj = new Outer.Inner(); // gọi trực tiếp, không cần Outer
        obj.show();

        User.Address addr = new User.Address("Hà Nội", "Việt Nam");
        addr.show(); // Hà Nội, Việt Nam

        Student s1 = new Student("Phương");
        Student s2 = new Student("Phúc");

        s1.showInfo();
        s2.showInfo();

        System.out.println(Config.dbUrl);
    }
}
