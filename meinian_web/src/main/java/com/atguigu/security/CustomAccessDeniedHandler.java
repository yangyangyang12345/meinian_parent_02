package com.atguigu.security;


import com.alibaba.fastjson.JSON;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException, ServletException {


        if (isAjaxRequest(request)){
            Result result = new Result(false, "无权访问", "403");
            String json = JSON.toJSONString(result);//把任意类型的转化为json串
            response.getWriter().print(json);
        }else {
        //    同步请求处理
            request.getRequestDispatcher("/pages/common/403.html").forward(request,response);
        }





    }

    //判断是异步请求还是同步请求
    public static boolean isAjaxRequest(HttpServletRequest request){
        if (request.getHeader("accept").indexOf("application/json") > -1  || (request.getHeader("X-Requested-With")!=null&&
                request.getHeader("X-Requested-With").equalsIgnoreCase("XMLHttpRequest"))){
            return true;
        }
        return false;
    }
}
