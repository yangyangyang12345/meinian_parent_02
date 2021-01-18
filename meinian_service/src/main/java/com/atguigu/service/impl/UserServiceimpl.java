package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.UserDao;
import com.atguigu.pojo.User;
import com.atguigu.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class UserServiceimpl implements UserService {
    @Autowired
    UserDao userDao;




//    根据用户名查询user对象

    @Override
    public User findUserByUsername(String username) {
       User user = userDao.findUserByUsername(username);
        return user;
    }
}
