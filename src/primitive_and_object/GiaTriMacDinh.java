package primitive_and_object;

public class GiaTriMacDinh {
    int primitiveInt;       // int (primitive)
    Integer objectInteger;  // Integer (object)
    String text;            // String (object)
    boolean flag;           // boolean (primitive)
    Boolean flagObj;        // Boolean (object)
    char kyTu;

    public static void main(String[] args) {
        GiaTriMacDinh d = new GiaTriMacDinh();

        System.out.println("primitive int: " + d.primitiveInt);     // 0
        System.out.println("object Integer: " + d.objectInteger);   // null
        System.out.println("String: " + d.text);                    // null
        System.out.println("primitive boolean: " + d.flag);         // false
        System.out.println("object Boolean: " + d.flagObj);         // null
        System.out.println("Char: " + d.kyTu);                      // rong
    }
}

