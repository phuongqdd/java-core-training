package string;

public class KhoiTao {
    public static void main(String[] args) {
        // 1. Dùng string literal
        String a = "Do phuong";
        String b = "Do phuong";

        System.out.println("a == b: " + (a == b));

        // 2. Dùng new
        String c = new String(a);
        System.out.println(c);

        System.out.println("a == c: " + (a == c));

        char[] charArray = {'G', 'o', 'o', 'd', ' ', 'D', 'a', 'y'};
        String message = new String(charArray);

        System.out.println(message);
    }
}
