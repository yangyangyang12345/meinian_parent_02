package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RequestMapping("/address")
@RestController
public class AddressController {
    @Reference
    AddressService addressService;

//    删除地址
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id){
        addressService.deleteById(id);
        return new Result(false,"删除地址成功");
    }




//    添加地址到数据库
    @RequestMapping("/addAddress")
    public Result addAddress(@RequestBody Address address){

        addressService.addAddress(address);
        return new Result(true,"添加地址成功");

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult pageResult = addressService.findPage(queryPageBean);
        return pageResult;
    }
  /*@RequestMapping("/findPage")
  public PageResult findPage(@RequestBody QueryPageBean queryPageBean) {

PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
      Page Page = addressService.findPage(queryPageBean.getQueryString());
      return  new PageResult(Page.getTotal(), Page.getResult());


  }*/
//    查询所有的分工地地址
    @RequestMapping("/findAllMaps")
    public Map findAllMaps(){
        Map<String, Object> map = new HashMap<>();
       List<Map> gridnMaps = new ArrayList<>();
        List<Map> nameMaps = new ArrayList<>();
        List<Address> list = addressService.findAllAddress();//如果返回来的就是集合name可以不用new,相当于返回的集合引用给了map,
        for (Address address : list) {
            Map<String, Object> gridnMap = new HashMap<>();
            gridnMap.put("lng",address.getLng());
            gridnMap.put("lat",address.getLat());
            gridnMaps.add(gridnMap);
            Map<String, Object> nameMap = new HashMap<>();
            nameMap.put("addressName",address.getAddressName());
            nameMaps.add(nameMap);
        }



        map.put("gridnMaps",gridnMaps);
        map.put("nameMaps",nameMaps);
        return map;
    }
}
