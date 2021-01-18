package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;


import com.atguigu.constant.MessageConstant;

import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.PageHelper;
import org.apache.xmlbeans.impl.xb.xsdschema.Public;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.PublicKey;
import java.util.List;


@RestController//@Controller 注解是controller  @ResponseBody  每个方法请求数据可以是json
@RequestMapping(value = "/travelGroup")
public class TravelGroupController {
    @Reference
    TravelGroupService travelGroupService;
//查询所有跟团游
    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<TravelGroup> list = travelGroupService.findAll();
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,list);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

//编辑跟团行  查询回显跟团行
    @RequestMapping("/findById")
   public Result findTravelGroupById(Integer id){
        try {
            TravelGroup travelGroup = travelGroupService.findTravelGroupById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELGROUP_SUCCESS,travelGroup);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELGROUP_FAIL);
        }
    }

    @RequestMapping("/findTravelItemIdsByTravelGroupId")
    public Result findTravelItemIdsByTravelGroupId(Integer id){
        try {
            List<Integer> travelItemIds =  travelGroupService.findTravelItemIdsByTravelGroupId(id);
            return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItemIds) ;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL) ;
        }

    }

    @RequestMapping("/edit")
public Result edit(Integer[] travelItemIds,@RequestBody TravelGroup travelGroup){

        try {
            travelGroupService.edit(travelItemIds,travelGroup);

            return new Result(true,MessageConstant.EDIT_TRAVELGROUP_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_TRAVELGROUP_FAIL);
        }

    }



@RequestMapping("/add")
    public Result add(Integer[] travelItemIds , @RequestBody TravelGroup travelGroup){
//    操作两张表。一张表示跟团游，另一张表示关联表
    try {
        travelGroupService.add(travelItemIds,travelGroup);
        return new Result(true,MessageConstant.ADD_TRAVELGROUP_SUCCESS);
    } catch (Exception e) {
        e.printStackTrace();
        return new Result(false,MessageConstant.ADD_TRAVELGROUP_FAIL);
    }
}



@RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
    PageResult pageResult = travelGroupService.findPage(queryPageBean.getCurrentPage(),
            queryPageBean.getPageSize(),
            queryPageBean.getQueryString());
            return pageResult;
}

}
