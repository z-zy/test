package com.example.test.test;

public class Test {
    public static void main(String[] args) {
        User user = new User();
        System.out.println("用户名"+user.getName()+"---");
        if (user.getName() == '\0'){
            System.out.println("char为空");
        }
    }
}
