package com.atguigu.dao;

import com.atguigu.pojo.Role;

public interface RoleDao {

    Role findRoleByUserId(Integer id);
}
