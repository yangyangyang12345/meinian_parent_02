package com.atguigu.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.TravelItemDao;
import com.atguigu.entity.PageResult;
import com.atguigu.pojo.TravelItem;
import com.atguigu.service.TravelItemService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;


@Service(interfaceClass = TravelItemService.class)
@Transactional
public class TravelItemServiceImpl implements TravelItemService {


    @Autowired
    private TravelItemDao travelItemDao;
    @Override
    public void add(TravelItem travelItem) {
        travelItemDao.add(travelItem);
    }

    @Override
    public PageResult findPage(Integer currentPage, Integer pageSize, String queryString) {
//        利用mybatis pagehelper 插件进行分页查询
//        开始分页查询功能
        PageHelper.startPage(currentPage,pageSize);
      Page<TravelItem> page =  travelItemDao.findPage(queryString);

        return new PageResult(page.getTotal(),page.getResult());
    }
//删除自由行
    @Override
    public void delete(Integer id) {
        travelItemDao.delete(id);
    }
//    查询回显自由行对象

    @Override
    public TravelItem getTravelItemById(Integer id) {
      TravelItem travelItem =  travelItemDao.getTravelItemById(id);


        return travelItem;
    }
//    编辑自由行

    @Override
    public void edit(TravelItem travelItem) {
        travelItemDao.edit(travelItem);
    }
    //查询所有自由行给跟团行的添加
    @Override
    public List<TravelItem> findAll() {
        List<TravelItem> list = travelItemDao.findAll();
        return list;
    }
}
