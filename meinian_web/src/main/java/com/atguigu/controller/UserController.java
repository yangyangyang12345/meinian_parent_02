package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;

import com.atguigu.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/user")
@RestController
public class UserController {


   // @Reference
//    UserService userService;
    @RequestMapping("/getUsername")
    public Result getUsername(){
//        认证成功后，框架将用户信息保存在session域中
//        securityContextHolder 工具类    securityContextHolder.getContext() 返回securityContext
        try {
            User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            return new Result(true, MessageConstant.GET_USERNAME_SUCCESS,user.getUsername());
        } catch (Exception e) {
            return  new Result(false,MessageConstant.GET_USERNAME_FAIL);
        }

    }
}

