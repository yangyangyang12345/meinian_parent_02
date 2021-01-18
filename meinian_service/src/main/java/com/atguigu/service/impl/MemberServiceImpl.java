package com.atguigu.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.atguigu.dao.MemberDao;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

@Service
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;

//根据月份查询注册会员数量


    @Override
    public List<Integer> findMemberCountByMonth(List<String> months) {
       List<Integer> list = new ArrayList<>();
        for (String month : months) {
            int count = memberDao.findMemberCountByMonth(month);
            list.add(count);
        }
        return list;
    }

    @Override
    public Member findByTelephone(String telephone) {
        Member member = memberDao.findByTelephone(telephone);


        return member;
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);


    }
}
