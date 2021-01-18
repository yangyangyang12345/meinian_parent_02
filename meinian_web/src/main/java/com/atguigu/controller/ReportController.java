package com.atguigu.controller;


import com.alibaba.dubbo.config.annotation.Reference;
import com.atguigu.constant.MessageConstant;
import com.atguigu.entity.Result;
import com.atguigu.pojo.Member;
import com.atguigu.pojo.Setmeal;
import com.atguigu.service.MemberService;
import com.atguigu.service.OrderService;
import com.atguigu.service.ReportService;
import com.atguigu.service.SetmealService;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    MemberService memberService;

@Reference
    SetmealService setmealService;
    @Reference
    OrderService orderService;
    @Reference
    ReportService reportService;


//    把表格数据导出，先准备表模板在一个一个往里写入
    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response) {
        try {
            //远程调用报表服务获取报表数据
            Map<String, Object> result = reportService.getBusinessReportData();

            //取出返回结果数据，准备将报表数据写入到Excel文件中
            String reportDate = (String) result.get("reportDate");
            Integer todayNewMember = (Integer) result.get("todayNewMember");
            Integer totalMember = (Integer) result.get("totalMember");
            Integer thisWeekNewMember = (Integer) result.get("thisWeekNewMember");
            Integer thisMonthNewMember = (Integer) result.get("thisMonthNewMember");
            Integer todayOrderNumber = (Integer) result.get("todayOrderNumber");
            Integer thisWeekOrderNumber = (Integer) result.get("thisWeekOrderNumber");
            Integer thisMonthOrderNumber = (Integer) result.get("thisMonthOrderNumber");
            Integer todayVisitsNumber = (Integer) result.get("todayVisitsNumber");
            Integer thisWeekVisitsNumber = (Integer) result.get("thisWeekVisitsNumber");
            Integer thisMonthVisitsNumber = (Integer) result.get("thisMonthVisitsNumber");
            List<Map> hotSetmeal = (List<Map>) result.get("hotSetmeal");

            //获得Excel模板文件绝对路径
            //file.separator这个代表系统目录中的间隔符，说白了就是斜线。
            String temlateRealPath = request.getSession().getServletContext().getRealPath("template") +
                    File.separator + "report_template.xlsx";

            //读取模板文件创建Excel表格对象
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(temlateRealPath)));
            XSSFSheet sheet = workbook.getSheetAt(0);

            XSSFRow row = sheet.getRow(2);
            row.getCell(5).setCellValue(reportDate);//日期

            row = sheet.getRow(4);
            row.getCell(5).setCellValue(todayNewMember);//新增会员数（本日）
            row.getCell(7).setCellValue(totalMember);//总会员数

            row = sheet.getRow(5);
            row.getCell(5).setCellValue(thisWeekNewMember);//本周新增会员数
            row.getCell(7).setCellValue(thisMonthNewMember);//本月新增会员数

            row = sheet.getRow(7);
            row.getCell(5).setCellValue(todayOrderNumber);//今日预约数
            row.getCell(7).setCellValue(todayVisitsNumber);//今日出游数

            row = sheet.getRow(8);
            row.getCell(5).setCellValue(thisWeekOrderNumber);//本周预约数
            row.getCell(7).setCellValue(thisWeekVisitsNumber);//本周出游数

            row = sheet.getRow(9);
            row.getCell(5).setCellValue(thisMonthOrderNumber);//本月预约数
            row.getCell(7).setCellValue(thisMonthVisitsNumber);//本月出游数

            int rowNum = 12;
            for (Map map : hotSetmeal) {//热门套餐
                String name = (String) map.get("name");
                Long setmeal_count = (Long) map.get("setmeal_count");
                BigDecimal proportion = (BigDecimal) map.get("proportion");
                row = sheet.getRow(rowNum++);
                row.getCell(4).setCellValue(name);//套餐名称
                row.getCell(5).setCellValue(setmeal_count);//预约数量
                row.getCell(6).setCellValue(proportion.doubleValue());//占比
            }

            //通过输出流进行文件下载
            ServletOutputStream out = response.getOutputStream();
            // 下载的数据类型（excel类型）
            response.setContentType("application/vnd.ms-excel");
            // 设置下载形式(通过附件的形式下载)
            response.setHeader("content-Disposition", "attachment;filename=report.xlsx");
            workbook.write(out);

            out.flush();
            out.close();
            workbook.close();

            return null;
        } catch (Exception e) {
            return new Result(false, MessageConstant.GET_BUSINESS_REPORT_FAIL, null);
        }

    }
//运营数据统计（页面）
    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        Map<String, Object> map = new HashMap<>();
       map = reportService.getBusinessReportData();
       return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
    }

//    饼状图的数据，套餐名称和每个套餐预定的数量封装到map集合中，用结果result返回
    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        List<Map> setmealCount = new ArrayList<>();
        setmealCount = setmealService.getSetmealReport();
        List<String> setmealNames = new ArrayList<>();
        for (Map map : setmealCount) {
            String name = (String)map.get("name");
            setmealNames.add(name);
        }
        Map<String, Object> map = new HashMap<>();
        map.put("setmealNames",setmealNames);
        map.put("setmealCount",setmealCount);


        return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
    }






//折线图的数据

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){


        List<String> months = new ArrayList<>();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH,-12);
        for (int i = 0; i < 12; i++) {
            calendar.add(Calendar.MONTH,1);
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM");
            months.add(simpleDateFormat.format(calendar.getTime()));
        }


        List<Integer> memberCount = new ArrayList<>();

        memberCount =  memberService.findMemberCountByMonth(months);

        Map<String, Object> data = new HashMap<>();
        data.put("months",months);
        data.put("memberCount",memberCount);

        return   new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,data);
    }

}
