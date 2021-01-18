package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.constant.RedisMessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Response;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/login")
public class loginController {


    @Autowired
    JedisPool jedisPool;


    @Reference
    MemberService memberService;
    @RequestMapping("/check")
    public Result login(@RequestBody Map map , HttpServletResponse response){
        String telephone = (String)map.get("telephone");
        String validateCode = (String) map.get("validateCode");
//        通过手机号处理获得redis里的value
        String code = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if(code == null || !code.equals(validateCode)){
           return new Result(false, MessageConstant.VALIDATECODE_ERROR);
        }else {
//            手机验证码验证没有问题，判断该用户是否是会员，如果不是会员注册为会员
         Member member =  memberService.findByTelephone(telephone);
//           进入业务逻辑层操作
            if (member == null){
//                当前用户不是会员，自动完后才能注册
                member = new Member();
                member.setPhoneNumber(telephone);
                member.setRegTime(new Date());
                memberService.add(member);
            }
//            登陆成功
//            写入cookie，跟踪用户，设置30天免登录
            Cookie cookie = new Cookie("login_member_telephone", telephone);
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);//有效期30天
            response.addCookie(cookie);//cookie为什么放到respon里
            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }
    }
}
