package oop;

public class C extends D  implements A, B {

	@Override
	public void run() {
		// TODO Auto-generated method stub
		
	}

	public static void main(String[] args) {
		System.out.println("COn điên");
		C obj = new C();
        obj.run();
	}

	@Override
	public int cong(int a) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int cong(int a, int b) {
		// TODO Auto-generated method stub
		return 0;
	}

}
