package com.atguigu.service;

import com.atguigu.entity.Result;

import java.util.Map;

public interface OrderService {


    Result submitOrder(Map map);

    Map findById(Integer id);



}
