package oop;

public class Student {
	void run() {
		System.out.println("HiHI");
	}
	
	int run(int a, int b) {
		return a + b;
	}
	
	class Calculator {
	    int add(int a, int b) {
	        return a + b;
	    }

	    double add(double a, double b) {
	        return a + b;
	    }

		double add(int a, double b) {
			return a + b;
		}

	    int add(int a, int b, int c) {
	        return a + b + c;
	    }
	}


}
