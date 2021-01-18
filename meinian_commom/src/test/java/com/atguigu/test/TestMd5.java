package com.atguigu.test;

import com.atguigu.util.MD5Utils;

public class TestMd5 {

    public static void main(String[] args) {
        String md5 = MD5Utils.md5("123");
        System.out.println("md5 == " + md5);//202cb962ac59075b964b07152d234b70
    }
}
