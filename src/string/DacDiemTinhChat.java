package string;

public class DacDiemTinhChat {
    public static void main(String[] args) {
        // 1. Immutable (Bất biến)
        String s1 = "Hello";
        String s2 = s1.concat(" World");
        System.out.println("s1: " + s1);
        System.out.println("s2: " + s2);

        // 2. String Pool
        String a = "Java";
        String b = "Java";
        String c = new String("Java");
        System.out.println("a == b : " + (a == b));
        System.out.println("a == c : " + (a == c));

        String x = "Apple";
        String y = "Banana";
        System.out.println("x.compareTo(y): " + x.compareTo(y)); // < 0 vì "Apple" < "Banana"

        // 4. CharSequence (truy cập ký tự)
        char ch = s1.charAt(1);
        System.out.println("s1.charAt(1): " + ch); // 'e'

        // 5. Unicode (Internationalization)
        String vn = "Xin chào";
        String jp = "こんにちは";
        System.out.println(vn);
        System.out.println(jp);

        System.out.println("identityHashCode a: " + System.identityHashCode(a));
        System.out.println("identityHashCode b: " + System.identityHashCode(b));
        System.out.println("identityHashCode c: " + System.identityHashCode(c));

        String[] tokens = "Java,C?C#,C++".split("[.,:;?]");
        for (int i = 0; i < tokens.length; i++)
            System.out.println(tokens[i]);
    }
}
