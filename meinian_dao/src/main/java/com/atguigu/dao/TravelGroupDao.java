package com.atguigu.dao;

import com.atguigu.pojo.TravelGroup;
import com.github.pagehelper.Page;

import java.util.List;
import java.util.Map;

public interface TravelGroupDao {
    void add(TravelGroup travelGroup);

    void addTravelGroupAndTravelItem(Map<String, Integer> map);

    Page findPage(String queryString);//TravelGroup的集合怎么转化为Page类型的？？？

    TravelGroup findTravelGroupById(Integer id);

    List<Integer> findTravelItemIdsByTravelGroupId(Integer id);

    void edit(TravelGroup travelGroup);

    void deleteTravelGroupAndTravelItemByTravelGroupId(Integer id);

    List<TravelGroup> findAll();
//辅助方法
    List<TravelGroup> findTravelGroupsById(Integer Id);
}
