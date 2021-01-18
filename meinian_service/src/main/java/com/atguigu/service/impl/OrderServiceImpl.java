package com.atguigu.service.impl;

import com.alibaba.druid.support.spring.stat.SpringStatUtils;
import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.MessageConstant;
import com.atguigu.dao.MemberDao;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.OrderSettingDao;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Order;
import com.atguigu.pojo.OrderSetting;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.OrderService;
import com.atguigu.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service(interfaceClass =OrderService.class )
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    OrderDao orderDao;
    @Autowired
    OrderSettingDao orderSettingDao;
    @Autowired
    MemberDao memberDao;
    @Autowired
    SetmealDao setmealDao;




//    查询order信息，包括查会员名和套餐名

    @Override
    public Map findById(Integer id) {
        Map<String, Object> map = new HashMap<>();
        Order order = orderDao.findOrderById(id);
        System.out.println(order.getMemberId() + "==="+ order.getSetmealId());
       Member member = memberDao.findMemberById(order.getMemberId());
       Setmeal setmeal = setmealDao.findSetmealById(order.getSetmealId());
        map.put("member",member.getName());
        map.put("setmeal",setmeal.getName());
        map.put("orderDate",order.getOrderDate());
        map.put("orderType", order.getOrderType());
        return map;
    }




//套餐预约

    /**
     * 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约
     * 2、检查用户所选择的预约日期是否已经约满，如果已经约满则无法预约
     * 3、检查用户是否重复预约（同一个用户在同一天预约了同一个套餐），如果是重复预约则无法完成再次预约
     * 4、检查当前用户是否为会员，如果是会员则直接完成预约，如果不是会员则自动完成注册并进行预约
     * 5、预约成功，更新当日的已预约人数
     * @param map
     * @return
     */
    @Override
    public Result submitOrder(Map map) {

// 1、检查用户所选择的预约日期是否已经提前进行了预约设置，如果没有设置则无法进行预约

//        获得预约日期，查询数据库是否存在  OrderSetting
        String orderDate = (String) map.get("orderDate");
//        字符串类型转化为日期类型
        Date date = null;
        try {
            date = DateUtils.parseString2Date(orderDate);
        } catch (Exception e) {
            e.printStackTrace();
        }
//查询数据库是否存在这个日期
      OrderSetting orderSetting = orderSettingDao.findByOrderDate(date);

        if (orderSetting == null){
//            不存在这个日期的套餐，返回错误
            return new  Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

//        不为空，检查已预约认识和最大预约人数
        if (orderSetting.getReservations()>=orderSetting.getNumber()){
            return new Result(false,MessageConstant.ORDER_FULL);
        }

//        判断这个人是否是会员

        String telephone = (String) map.get("telephone");
//        通过手机号查询数据库是否有这个会员
       Member member = memberDao.findByTelephone(telephone);
       if (member == null){//没有这个会员，创建会员，会员信息
           /*  private String name;//姓名
                private String sex;//性别
                private String idCard;//身份证号
                private String phoneNumber;//手机号*/
           member = new Member();
           String name = (String) map.get("name");
           member.setName(name);
           String sex = (String) map.get("sex");
           member.setSex(sex);
           String idCard = (String) map.get("idCard");
           member.setIdCard(idCard);
           String phoneNumber = (String) map.get("telephone");
           member.setPhoneNumber(phoneNumber);
           Date date1 = new Date();
           member.setRegTime(date1);
           memberDao.add(member);
       }

//       存在会员需判断这个日期是否已经预定过套餐  通过手机号查询
//        这里做一个通用方法，通过条件查询order  动态sql
        Integer id = member.getId();

        int setmealId = Integer.parseInt((String) map.get("setmealId"));
        Order order = new Order(member.getId(),date,null,null,setmealId);
        List<Order> list =  orderDao.findOrderByCondition(order);
        if (list != null && list.size() > 0){
//            该会员该日期该套餐已经订过了   需要几个条件查  3个条件查
           return new Result(false,MessageConstant.HAS_ORDERED);
        }
//        可以预约，预约人数加1放回数据库
        orderSetting.setReservations(orderSetting.getReservations() + 1);
//        更新数据库
//        预定套餐信息添加
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setOrderType(Order.ORDERTYPE_WEIXIN);
        orderDao.add(order);
//        套餐可预定人数更新
        orderSettingDao.editReservations(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order);

    }
}
