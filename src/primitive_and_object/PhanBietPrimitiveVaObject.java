package primitive_and_object;

public class PhanBietPrimitiveVaObject {
    public static void main(String[] args) {
        // ===== Primitive =====
        int a = 10;
        int b = a; // copy giá trị trực tiếp
        b = 20;    // thay đổi b không ảnh hưởng đến a

        System.out.println("Primitive:");
        System.out.println("a = " + a); // vẫn là 10
        System.out.println("b = " + b); // đã đổi thành 20


        // ===== Object (Wrapper) =====
        Integer x = Integer.valueOf(10);
        Integer y = x; // copy tham chiếu (reference)
        y = 20;        // y trỏ sang giá trị mới, x vẫn giữ 10 (wrapper là immutable)

        System.out.println("\nObject (Wrapper):");
        System.out.println("x = " + x);
        System.out.println("y = " + y);


        // ===== Object (Array) =====
        int[] arr1 = {1, 2, 3};
        int[] arr2 = arr1; // copy tham chiếu
        arr2[0] = 99;      // thay đổi qua arr2, arr1 cũng bị đổi

        System.out.println("\nObject (Array):");
        System.out.println("arr1[0] = " + arr1[0]); // 99
        System.out.println("arr2[0] = " + arr2[0]); // 99
    }
}
