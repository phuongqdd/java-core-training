package array;

import java.util.Arrays;

public class Cau1 {
    // Array là là một tập hợp các phần tử có cùng kiểu được lưu trữ gần nhau trong bộ nhớ.
    // Các phần tử trong mảng được đánh số chỉ mục (index) bắt đầu từ 0.
    // Kích thước mảng cố định sau khi khởi tạo.

    // Các đặc điểm quan trọng
    //      Index bắt đầu từ 0
    //      Kích thước cố định sau khi khởi tạo
    //      Kiểu phần tử đồng nhất
    //      Truy cập nhanh theo chỉ số
    //      Mảng có thể là primitive type hoặc object

    //Truyền mảng vào phương thức

    public static void printArray(int[] array) {
        for (int number : array) {
            System.out.print(number + " ");
        }
        System.out.println();
    }


    //Trả về mảng từ phương thức
    public static int[] createArray(int size) {
        int[] newArray = new int[size];
        for (int i = 0; i < size; i++) {
            newArray[i] = i + 1;
        }
        return newArray;
    }

    // Sử dụng mảng tham so bien
    public static void printVarargs(int... numbers) {
        for (int number : numbers) {
            System.out.print(number + " ");
        }
        System.out.println();
    }

    public static void main(String[] args) {
        // Khai báo khởi tạo
        int[] arr = new int[3];
        String[] names = new String[2];

        // Khai báo + khởi tạo giá trị trực tiếp
        int[] arr1 = {1, 2, 3};

        // Truy xuất và gán giá trị
        int first = arr1[0];
        arr1[1] = 10;


        // Mang 1 chieu
        int[] a = {1, 4, 3, 6, 4};
        for(int i = 0; i < a.length; i++){
            System.out.println(a[i]);
        }

        //Sắp xếp mảng
        Arrays.sort(a);
        System.out.println("Mảng sau khi được saáp xếp");
        for(int tmp : a){
            System.out.println(tmp);
        }

        // Maảng nhiều chiều
        int [][] b = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        System.out.println(b[1][2]);

        int[][][] cube = new int[2][3][4];

        String[] names1 = {"Alice", "Bob"};
        System.out.println("names1[0] = " + names1[0]);

        // Sao chép mảng
        int[] copy = Arrays.copyOf(arr, arr.length);

        System.out.println("Truyền mảng vào phương thức");
        printArray(arr1);

        System.out.println("Trả về mảng từ phương thức");
        int[] arr2 = createArray(10);
        printArray(arr2);

        System.out.println("Mảng tham số biến");
        printVarargs(1, 2, 3, 4, 1000, 20);
    }
}
