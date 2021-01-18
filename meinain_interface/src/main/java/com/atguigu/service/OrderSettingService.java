package com.atguigu.service;

import com.atguigu.pojo.OrderSetting;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public interface OrderSettingService {

    void add(ArrayList<OrderSetting> orderSettings);

    List<Map> getOrderSettingByMonth(String date);

    void editNumberByDate(OrderSetting orderSetting);
}
