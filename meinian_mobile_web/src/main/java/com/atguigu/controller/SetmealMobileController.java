package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;

import com.atguigu.pojo.Setmeal;
import com.atguigu.service.SetmealService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController//相当于@controller 和 @responsebody
@RequestMapping("/setmeal")
public class SetmealMobileController {

@Reference
    SetmealService setmealService;


//根据id查询单个套餐简单信息
    @RequestMapping("/getSetmealById")
    public Result getSetmealById(Integer id){
       Setmeal setmeal = setmealService.getSetmealById(id);
       return new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }



//查询套餐的详细信息
    @RequestMapping("/findById")
    public Result findSetmealById(Integer id){//是否可以用字符串接
      Setmeal setmeal = setmealService.findSetmealById(id);
      return  new Result(true,MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);

    }

//查询所有的套餐列表
    @RequestMapping("/getSetmeal")
    public Result findAllSetmeal(){

      List<Setmeal> list = setmealService.findAllSetmeal();
      return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,list);
    }
}
