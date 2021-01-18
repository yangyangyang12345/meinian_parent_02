package com.atguigu.dao;

import com.atguigu.pojo.Member;

public interface MemberDao {

  Member findByTelephone(String telephone);

    void add(Member member);

  Member findMemberById(Integer memberId);

    int findMemberCountByMonth(String month);

  int getTodayNewMember(String today);

  int getTotalMember();

  int getThisWeekAndMonthNewMember(String weekMonday);
}
