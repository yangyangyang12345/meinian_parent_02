package com.atguigu.dao;

import com.atguigu.pojo.Permission;

public interface PermissionDao {

    Permission findPermissionsByRoleId(Integer id);
}
