package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.constant.RedisConstant;
import com.atguigu.dao.OrderDao;
import com.atguigu.dao.SetmealDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    SetmealDao setmealDao;

   @Autowired
    JedisPool jedisPool;
   @Autowired
    OrderDao orderDao;

//   查询饼状图信息

    @Override
    public List<Map> getSetmealReport() {
        List<Map> list = new ArrayList<>();
     list =  orderDao.getSetmealReport();




        return list;
    }


//查询单个套餐信息，简单信息

    @Override
    public Setmeal getSetmealById(Integer id) {
      Setmeal setmeal =  setmealDao.getSetmealById(id);
      return setmeal;

    }


//   查询套餐详细信息

    @Override
    public Setmeal findSetmealById(Integer id) {
       Setmeal setmeal = setmealDao.findSetmealById(id);
        return setmeal;

    }


//   查询所有套餐信息


    @Override
    public List<Setmeal> findAllSetmeal() {
      List<Setmeal> list = setmealDao.findAllSetmeal();

        return list;
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);//将参数数据通过ThreadLocal与当前线程进行绑定。
        Page page = setmealDao.findPage(queryString);
        return new PageResult(page.getTotal(),page.getResult());//page.getResult()返回的是页面对象的集合
    }
//添加套餐

    @Override
    public void add(Integer[] travelgroupIds, Setmeal setmeal) {
        setmealDao.add(setmeal); //主键回填,自增的主键只能返回到service层，既调用者
        if(travelgroupIds!=null && travelgroupIds.length>0){
            for (Integer travelgroupId : travelgroupIds) {
                Map<String,Integer> map = new HashMap<>();
                map.put("travelgroupId",travelgroupId);
                map.put("setmealId",setmeal.getId());
                setmealDao.addSetmealAndTravelGroup(map);
            }
        }

        //*******补充代码 解决垃圾图片问题**********************************
       jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        //*****************************************
    }
}

