package com.atguigu.dao;

import com.atguigu.pojo.TravelItem;
import com.github.pagehelper.Page;

import java.util.List;

public interface TravelItemDao {
    void add(TravelItem travelItem);

    Page<TravelItem> findPage(String queryString);

    void delete(Integer id);

    TravelItem getTravelItemById(Integer id);

    void edit(TravelItem travelItem);

    List<TravelItem> findAll();
//    辅助方法
    List<TravelItem>  findTravelItemsById(Integer id);

}
