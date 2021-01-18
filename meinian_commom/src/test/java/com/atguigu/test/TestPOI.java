package com.atguigu.test;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class TestPOI {
    @Test
    public void readExcel() {
//        工作簿
        XSSFWorkbook workbook = null;
        try {
              workbook = new XSSFWorkbook("D:\\hello.xlsx");
        } catch (IOException e) {
            e.printStackTrace();
        }
//        工作表
        XSSFSheet sheet = workbook.getSheetAt(0);
//        对工作表的行进行迭代，获取行
        for (Row row : sheet) {
//        对行进行迭代，获取单元格
            for (Cell cell : row) {
//        获取单元格数据
                String value = cell.getStringCellValue(); //注意：数字类型,需要修改excel单元格的类型，否则报错。
                System.out.println(value);// new String(value.getBytes("UTF-8"),"GBK"));
//        关闭工作簿
            }
        }
        try {
            workbook.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    public void readExcel2(){
//        创建工作簿
        try {
            XSSFWorkbook workbook = new XSSFWorkbook("d:/hello.xlsx");
//            获取工作表，根据工作表的顺序获取也可以根据工作表的名称获取
            XSSFSheet sheet = workbook.getSheetAt(0);
//            获取工作表的最后一行的行号，行号从0开始
            int lastRowNum = sheet.getLastRowNum();
            for (int i = 0; i <= lastRowNum; i++) {
//                根据行号获取行对象
                XSSFRow row = sheet.getRow(i);
//                在获取单元格对象
                short lastCellNum = row.getLastCellNum();
                for(int j=0 ; j<lastCellNum; j++){
//                    获取单元格对象的值
                    String value = row.getCell(j).getStringCellValue();
                    System.out.print(value + " ");
                }
                System.out.println();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
//    向excel写数据
    @Test
    public void importExcel(){
//        在内存中创建一个excel文件
        XSSFWorkbook workbook = new XSSFWorkbook();
//        创建工作表，指定工作表名称
        XSSFSheet sheet = workbook.createSheet("尚硅谷");
//        创建行，0表示第一行
        XSSFRow row = sheet.createRow(0);
//        创建单元格，0表示第一个单元格
        row.createCell(0).setCellValue("编号");
        row.createCell(1).setCellValue("姓名");
        row.createCell(2).setCellValue("年龄");


        XSSFRow row1 = sheet.createRow(1);
        row1.createCell(0).setCellValue("1");
        row1.createCell(1).setCellValue("小明");
        row1.createCell(2).setCellValue("11");

        XSSFRow row2 = sheet.createRow(2);
        row2.createCell(0).setCellValue("2");
        row2.createCell(1).setCellValue("小红");
        row2.createCell(2).setCellValue("22");
//        通过输出流将workbook对象下载到磁盘
        try {
            OutputStream out = new FileOutputStream("D:/atgui.xlsx");
            workbook.write(out);
            out.flush();//刷新缓存到磁盘
            out.close();
            workbook.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}