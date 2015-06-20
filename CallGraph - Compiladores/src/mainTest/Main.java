package mainTest;

class A {
	int val;
	
	A(int val) {
		this.val = val;
	}
	
	void inc() {
		this.val++;
	}
}

class B {
	A objA;
	
	B(A objA) {
		this.objA = objA;
	}
	
	void inc() {
		this.objA.inc();
	}
}

public class Main {
	public static void main(String[] args) {
		A a = new A(3);
		B b = new B(a);
		
		a.inc();  // a.val = 4
		b.inc();  // a.val = 5
	}
}
