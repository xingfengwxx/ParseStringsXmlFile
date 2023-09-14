package com.wangxingxing.tools;

public class Test {

    public static void main(String[] args) {
        try {
            int result = divide(10, 0);
            System.out.println("Result: " + result); // 这行代码不会被执行
        } catch (ArithmeticException e) {
            e.printStackTrace();
            System.out.println("Program continues after exception---0");
        }

        System.out.println("Program continues after exception---1"); // 这行代码会执行
    }

    public static int divide(int a, int b) {
        return a / b; // 这里会抛出ArithmeticException
    }

}
