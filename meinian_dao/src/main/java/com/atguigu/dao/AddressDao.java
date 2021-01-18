package com.atguigu.dao;

import com.atguigu.pojo.Address;
import com.github.pagehelper.Page;

import java.util.List;

public interface AddressDao {


    List<Address> findAllAddress();

    Page findPage(String getQueryString);

    void addAddress(Address address);

    void deleteById(Integer id);
}
