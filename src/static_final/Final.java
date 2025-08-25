package static_final;

import java.util.ArrayList;

// final method
class Parent {
    final void display() {
        System.out.println("Đây là phương thức final trong lớp cha");
    }
}

class Child extends Parent {
    // Không thể override:
    // void display() {
    //     System.out.println("Ghi đè phương thức");
    // }
}

//final class
final class Animal {
    void sound() {
        System.out.println("Animal sound");
    }
}

// Lỗi, không thể kế thừa final class
// class Dog extends Animal {}

interface Constants {
    int MAX_USERS = 100;  // thực chất là public static final int MAX_USERS = 100;
}
class Demo {
    final int a = 5;             // final → không đổi, mỗi object có 1 a
    static int b = 10;           // static → tất cả object dùng chung b, có thể đổi
    static final int c = 100;    // static final → hằng số, dùng chung cho tất cả, không đổi
}

public class Final {

    public static void main(String[] args) {
        final int x = 100;
        // x = 200; Lỗi, vì x là final nên không đổi được
        System.out.println("Giá trị của x: " + x);

        Child c = new Child();
        c.display();

        Animal a = new Animal();
        a.sound();

        System.out.println("Số user tối đa: " + Constants.MAX_USERS);

        final ArrayList<String> list = new ArrayList<>();
        list.add("Java");       // được
        list.add("Spring");     // được
        // list = new ArrayList<>(); Không được đổi object

        System.out.println(list); // [Java, Spring]

        Demo obj1 = new Demo();
        Demo obj2 = new Demo();

        System.out.println("obj1.a = " + obj1.a); // 5
        System.out.println("obj2.a = " + obj2.a); // 5

        Demo.b = 20; // thay đổi giá trị static
        System.out.println("obj1.b = " + obj1.b); // 20
        System.out.println("obj2.b = " + obj2.b); // 20

        System.out.println("c = " + Demo.c); // 100
        // Demo.c = 200; Lỗi, static final không đổi
    }
}
