package com.atguigu.test;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class TestSpringSecurity {

    public static void main(String[] args) {//MD5加盐加密 生成60位
        BCryptPasswordEncoder ps = new BCryptPasswordEncoder();
        String encode = ps.encode("123");
        System.out.println(encode);//$2a$10$04XweIw7DdRKZrZqz/gEceD31GtLAu6odJU04lGoklfhbOXzSo1OO
        //$2a$10$BazoPLGM6uwb8Fotd0SOD.LD56xgDjnpFgwO0I58c3OuyP/48suKu
    }

}
