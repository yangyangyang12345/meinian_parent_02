package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;

import com.atguigu.pojo.Order;
import com.atguigu.service.OrderService;
import com.atguigu.util.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;


@RestController
@RequestMapping("/order")
public class OrderMobileController {


    @Autowired
    JedisPool jedisPool;

    @Reference
    OrderService orderService;

    @RequestMapping("/findById")
    public Result findById(Integer id){
       Map map =  orderService.findById(id);
       return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
    }





    @RequestMapping("/submit")
    public Result submitOrder(@RequestBody  Map  map){
//        1.在页面获取手机号
        String telephone = (String) map.get("telephone");
//        2.获取页面的验证码
        String validateCode = (String) map.get("validateCode");
//        3.获取redis里的验证码，对比是否相等 ,key为telephone+ RedisMessageConstant.SENDTYPE_ORDER
        String codeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        if (codeRedis == null || !codeRedis.equals(validateCode)){//验证码不争取
            return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }
//        调用旅游预约服务，进入业务逻辑层（查看该日期是否有套餐可以预定  和 套餐预定是否已经订满）
        map.put("orderType", Order.ORDERTYPE_WEIXIN);


        Result result = null;
        try {
            result = orderService.submitOrder(map);
        } catch (Exception e) {
            e.printStackTrace();
            return result;
        }
//       如果预约成功发送预约成功短信
        if (result.isFlag()){
            //预约成功，发送短信通知，短信通知内容可以是“预约时间”，“预约人”，“预约地点”，“预约事项”等信息
            String orderDate = (String) map.get("orderDate");
//            发送短信只需要调用发短信的工具类就可以
            try {
                SMSUtils.sendShortMessage(telephone,"8888");
            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("发送的验证码为：8888");
            }
        }

        return result;
    }

}
