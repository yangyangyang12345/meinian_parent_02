package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelGroupDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelGroup;
import com.atguigu.service.TravelGroupService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.parameters.P;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = TravelGroupService.class)
@Transactional
public class TravelGroupServiceImpl implements TravelGroupService{


    @Autowired
    TravelGroupDao travelGroupDao;

//查询所有跟团游


    @Override
    public List<TravelGroup> findAll() {
       List<TravelGroup> list = travelGroupDao.findAll();
        return list;
    }

    //   编辑跟团游 查询跟团游回显
    @Override
    public TravelGroup findTravelGroupById(Integer id) {
      TravelGroup travelGroup =  travelGroupDao.findTravelGroupById(id);
        return travelGroup;
    }

//    根据跟团游id，查询自由行的id集合
    @Override
    public List<Integer> findTravelItemIdsByTravelGroupId(Integer id) {

       List<Integer> list =  travelGroupDao.findTravelItemIdsByTravelGroupId(id);
       return  list;
    }
//    对编辑中的添加跟团游


    @Override
    public void edit(Integer[] travelItemIds, TravelGroup travelGroup) {

//        编辑跟团游
        travelGroupDao.edit(travelGroup);

//        删除此跟团游的中间表数据
        travelGroupDao.deleteTravelGroupAndTravelItemByTravelGroupId(travelGroup.getId());

        //再重新添加该跟团游的中间表数据
        setTravelGroupAndTravelItem(travelItemIds,travelGroup.getId());

    }

    //    添加跟团游
    @Override
    public void add(Integer[] travelItemIds, TravelGroup travelGroup) {
//        添加跟团游数据
        travelGroupDao.add(travelGroup);//保证主键回填，数据库分配主键值，框架将逐渐获取并封装到实体对象上

//        往中间表添加数据（一个跟团游对应多个自由行）
        setTravelGroupAndTravelItem(travelItemIds,travelGroup.getId());

    }
    private void setTravelGroupAndTravelItem(Integer[] travelItemIds,Integer travelGroupId){
        if(travelItemIds != null && travelItemIds.length>0){
            for (Integer travelItemId : travelItemIds) {
                Map<String, Integer> map = new HashMap<>();
                map.put("travelGroupId",travelGroupId);
                map.put("travelItemId",travelItemId);
                travelGroupDao.addTravelGroupAndTravelItem(map);
            }
        }
    }




//    分页查询带条件

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
        PageHelper.startPage(currentPage,pageSize);

       Page page = travelGroupDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }

}
