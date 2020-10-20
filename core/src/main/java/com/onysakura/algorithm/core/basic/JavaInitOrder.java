package com.onysakura.algorithm.core.basic;

/**
 * Java 类与继承关系中的代码初始化顺序
 */
public class JavaInitOrder {
    public static void main(String[] args) {
        new ChildClass();
    }
}

class ChildClass extends ParentClass {

    public ChildClass() {
        System.out.println("ChildClass construct");
    }

    {
        // 非静态代码块编译后会放到构造方法的第一行
        System.out.println("ChildClass block");
    }

    static {
        System.out.println("ChildClass static block");
    }
}

class ParentClass {

    public ParentClass() {
        System.out.println("ParentClass construct");
    }

    {
        System.out.println("ParentClass block");
    }

    static {
        System.out.println("ParentClass static block");
    }
}
