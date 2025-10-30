package oop;

public class Test {
    public String hihi(int a){
        System.out.println("HiHi 2");
        return a + "";
    }

    public void hihi(){
        System.out.println("Jijij");
    }

    public void hihi(String b){
        System.out.println("Jijij");
    }

    public static void main(String[] args) {
        Test test = new Test();
        test.hihi();
    }
}
