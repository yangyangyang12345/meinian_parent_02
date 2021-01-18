package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.service.OrderSettingService;
import org.apache.xmlbeans.impl.xb.xmlconfig.Extensionconfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {
@Autowired
OrderSettingDao orderSettingDao;


//设置可预约的人数

    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
//        查询是否有这个日期的数据
        Long count =  orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
        if (count > 0){
//              进行修改操作
            orderSettingDao.editNumberByOrderDate(orderSetting);
        }else {
            //进行添加操作
            orderSettingDao.add(orderSetting);

        }
    }

//查询预约设置数据

    @Override
    public List<Map> getOrderSettingByMonth(String date) {

//  组织查询的map，dateBegin表示月份开始时间，dateEnd月份结束时间
        String dateBegin = date + "-1";
        String dateEnd = date + "-31";
        Map<String, Object> map = new HashMap<>();
        map.put("dateBegin",dateBegin);
        map.put("dateEnd",dateEnd);
      List<OrderSetting> list = orderSettingDao.getOrderSettingByMonth(map);
//      把OrderSetting类型转化为后天能接收的map类型 ,由于数据多条，把map保存在list集合中
        ArrayList<Map> listResult = new ArrayList<>();

//        两种list集合转化，遍历list集合
        for (OrderSetting orderSetting : list) {
            Map<String, Object> data = new HashMap<>();
            data.put("date",orderSetting.getOrderDate().getDate() );
            data.put("number",orderSetting.getNumber());
            data.put("reservations",orderSetting.getReservations());
            listResult.add(data);
        }
        return listResult;

    }

    @Override
    public void add(ArrayList<OrderSetting> orderSettings) {
//        进行业务逻辑分析
//        根据日期查询数据的数量0/1
//        循环遍历集合
        for (OrderSetting orderSetting : orderSettings) {
//            判断当前的日期之前是否已经被设置过预约的日期，使用当前的时间作为条件查询
          Long count =  orderSettingDao.findCountByOrderDate(orderSetting.getOrderDate());
          if (count > 0){
//              进行修改操作
              orderSettingDao.editNumberByOrderDate(orderSetting);
          }else {
              //进行添加操作
              orderSettingDao.add(orderSetting);

          }
        }
    }
}
