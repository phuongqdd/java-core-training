package string;

public class SpringPool {
    // String pool là một vùng nhớ đặc biệt nằm trong vùng nhớ Heap (Heap memory),
    // dùng để lưu trữ các biến được khai báo theo kiểu String.
    //String Pool là 1 phân vùng nhỏ nằm trong bộ nhớ Heap chứa các Strings.
    //Nó được tạo ra với mục đích tối ưu lưu trữ và dùng vùng nhớ khi khai báo String từ đó giúp hạn chế tình trạng tràn bộ nhớ Heap


    // - Cach hoat dong
    // Khi tạo String literal "X":
    //   -> kiểm tra pool
    //       -> nếu có -> trỏ tới object đã có
    //       -> nếu chưa -> tạo object mới trong pool
    //
    //Khi tạo new String("X"):
    //   -> tạo object mới trên heap
    //
    //Khi gọi intern():
    //   -> trả về object trong pool

    // Mot so cach so sanh
    //Phương thức	        So sánh cái gì?	                         Ví dụ
    //==	                Tham chiếu (object)	                     s1 == s2
    //equals()	            Giá trị chuỗi	                         s1.equals(s2)
    //equalsIgnoreCase()	Giá trị chuỗi, bỏ qua hoa thường	     s1.equalsIgnoreCase(s2)
    //compareTo()	        Thứ tự từ điển (lexicographic)	         s1.compareTo(s2)
    public static void main(String[] args) {

        // 1. String literal (trong String Pool)
        String s1 = "Java";
        String s2 = "Java";

        // 2. String object bằng new (trên heap)
        String s3 = new String("Java");

        // 3. Sử dụng intern() để đưa về pool
        String s4 = s3.intern();

        // 4. So sánh tham chiếu (==)
        System.out.println("s1 == s2 : " + (s1 == s2)); // true, cùng pool
        System.out.println("s1 == s3 : " + (s1 == s3)); // false, s3 heap
        System.out.println("s1 == s4 : " + (s1 == s4)); // true, s4 pool

        // 5. So sánh giá trị (equals)
        System.out.println("s1.equals(s3) : " + s1.equals(s3)); // true

        // 4. So sánh giá trị không phân biệt hoa thường (equalsIgnoreCase)
        System.out.println("So sánh giá trị (bỏ qua hoa/thường):");
        System.out.println("s1.equalsIgnoreCase(s4) : " + s1.equalsIgnoreCase(s4)); // true
        System.out.println();

        // 5. So sánh thứ tự từ điển (compareTo)
        String a = "Apple";
        String b = "Banana";
        System.out.println("So sánh thứ tự từ điển:");
        System.out.println("a.compareTo(b) : " + a.compareTo(b)); // âm vì Apple < Banana
        System.out.println("b.compareTo(a) : " + b.compareTo(a)); // dương vì Banana > Apple
        System.out.println("a.compareTo(a) : " + a.compareTo(a)); // 0, bằng nhau

        // 6. Bất biến (immutable)
        String s5 = s1.concat(" Programming"); // tạo String mới
        System.out.println("s1 : " + s1); // Hello vẫn không thay đổi
        System.out.println("s5 : " + s5); // Java Programming

        // 7. Khởi tạo từ char array
        char[] chars = {'H','e','l','l','o'};
        String s6 = new String(chars);
        System.out.println("s6 : " + s6);

        // 8. Một số phương thức hữu ích
        System.out.println("Length of s5 : " + s5.length());
        System.out.println("Char at index 5 of s5 : " + s5.charAt(5));
        System.out.println("Substring s5 from 5 : " + s5.substring(5));
    }
}
