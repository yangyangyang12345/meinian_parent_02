package com.atguigu.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import org.springframework.aop.support.AopUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController//@Controller 注解是controller  @ResponseBody  每个方法请求数据可以是json
@RequestMapping(value = "/travelItem")
public class TravelItemController {
    @Reference
    private TravelItemService travelItemService;

//    删除自由行
    @RequestMapping("/delete")
    @PreAuthorize("hasAnyAuthority('TRAVELITEM_DELETE1')")
    public Result delete(Integer id){  //网页传的参数用相同名字接受就可以
        try {
            travelItemService.delete(id);
            return new Result(true,MessageConstant.DELETE_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_TRAVELITEM_FAIL);
        }
    }

//    编辑自由行 1,。回显数据

    @RequestMapping("/findById")
    public Result getTravelItemById(Integer id){  //网页传的参数用相同名字接受就可以
        try {
           TravelItem travelItem = travelItemService.getTravelItemById(id);
            return new Result(true,MessageConstant.QUERY_TRAVELITEM_SUCCESS,travelItem);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_TRAVELITEM_FAIL);
        }
    }
//    编辑自由行2.修改自由行信息
    @RequestMapping("/deit")
    @PreAuthorize("hasAnyAuthority('TRAVELITEM_EDIT')")
    public Result edit(@RequestBody TravelItem travelItem){//注解的作用接受页面传过来的json数据
        try {
            travelItemService.edit(travelItem);
            return new Result(true,MessageConstant.EDIT_TRAVELITEM_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return  new Result(false,MessageConstant.EDIT_TRAVELITEM_FAIL);
        }


    }


//    分页查询带条件
@RequestMapping("/findPage")
@PreAuthorize("hasAnyAuthority('TRAVELITEM_QUERY')")
public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
  PageResult pageResult =  travelItemService.findPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize(),queryPageBean.getQueryString());
  return pageResult;
}




    //新增
    @RequestMapping("/add")
    @PreAuthorize("hasAnyAuthority('TRAVELITEM_ADD')")
    public Result add(@RequestBody TravelItem travelItem){
      //  System.out.println(travelItem);
        try {
            travelItemService.add(travelItem);
            return new Result(true, MessageConstant.ADD_TRAVELITEM_SUCCESS);
        }catch (Exception e){
            return new Result(false, MessageConstant.ADD_TRAVELITEM_FAIL);
        }

    }

//    查询所有的自由行给跟团行
    @RequestMapping("/findAll")
    public Result findAll() {
        List<TravelItem> list = travelItemService.findAll();
        return new Result(true, MessageConstant.QUERY_TRAVELITEM_SUCCESS, list);
    }
}