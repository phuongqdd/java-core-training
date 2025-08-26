package memory;

public class Cau1 {

    static int staticVar = 10;    // Biến tatic, cấp phát 1 lần khi load class

    public static void main(String[] args){
        int a = 5; // biến local, được cấp pháp tính trong stack

        System.out.println(staticVar);
        System.out.println(a);

        // Cấp phát động
        String str = new String("Hello");
        int[] arr = new int[5];

    }

}
