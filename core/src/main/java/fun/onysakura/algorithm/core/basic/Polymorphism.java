package fun.onysakura.algorithm.core.basic;

public class Polymorphism {

    public static void main(String[] args) {
        System.out.println(new A("Jack"));
        System.out.println(new B(3000L));
        System.out.println(new C(16.24D));
    }
}

class A {
    private String name;

    public A(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "name:   " + name;
    }
}

class B {
    private Long amount;

    public B(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "amount: " + amount;
    }
}


class C {
    private Double height;

    public C(Double height) {
        this.height = height;
    }
}