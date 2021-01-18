package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.AddressDao;
import com.atguigu.entity.PageResult;
import com.atguigu.entity.QueryPageBean;
import com.atguigu.pojo.Address;
import com.atguigu.service.AddressService;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = AddressService.class)
@Transactional
public class AddressServiceImpl implements AddressService {

    @Autowired
    AddressDao addressDao;
//    删除地址


    @Override
    public void deleteById(Integer id) {
        addressDao.deleteById(id);
    }

    //    添加地址
  @Override
    public void addAddress(Address address) {
        addressDao.addAddress(address);
    }

   //分页查询带条件
    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(),queryPageBean.getPageSize());
        Page page = addressDao.findPage(queryPageBean.getQueryString());
        return new PageResult(page.getTotal(),page.getResult());
    }

 /* @Override
  public Page findPage(String getQueryString) {

      Page page = addressDao.findPage(getQueryString);
      return page;
  }
*/
    @Override
    public List<Address> findAllAddress() {

     List<Address>  list = addressDao.findAllAddress();
     return list;
    }
}
