package cn.iponkan.controller;


import cn.iponkan.entity.User;
import cn.iponkan.mapper.UserMapper;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.List;

@Controller
public class ExcelController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("/getExcel")
    public void getExcel (HttpServletResponse response) throws Exception {
        List<User> userList = userMapper.getAll();

        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet =wb.createSheet("获取excel测试表格");
        HSSFRow row = null;

        row = sheet.createRow(0);
        row.setHeight((short)(26.25*20));
        row.createCell(0).setCellValue("用户信息列表");
        row.getCell(0).setCellStyle(getStyle(wb,0));//设置样式
        for(int i = 1;i <= 3;i++){
            row.createCell(i).setCellStyle(getStyle(wb,0));
        }
        CellRangeAddress rowRegion = new CellRangeAddress(0,0,0,3);
        sheet.addMergedRegion(rowRegion);

        CellRangeAddress columnRegion = new CellRangeAddress(1,4,0,0);
        sheet.addMergedRegion(columnRegion);

        row = sheet.createRow(1);
        row.createCell(0).setCellStyle(getStyle(wb,3));
        row.setHeight((short)(22.50*20));
        row.createCell(1).setCellValue("用户Id");
        row.createCell(2).setCellValue("用户名");
        row.createCell(3).setCellValue("用户密码");
        for(int i = 1;i <= 3;i++){
            row.getCell(i).setCellStyle(getStyle(wb,1));
        }

        for(int i = 0;i<userList.size();i++){
            row = sheet.createRow(i+2);
            User user = userList.get(i);
            row.createCell(1).setCellValue(user.getId());
            row.createCell(2).setCellValue(user.getName());
            row.createCell(3).setCellValue(user.getAge());
            for(int j = 1;j <= 3;j++){
                row.getCell(j).setCellStyle(getStyle(wb,2));
            }
        }

        //默认行高
        sheet.setDefaultRowHeight((short)(16.5*20));
        //列宽自适应
        for(int i=0;i<=13;i++){
            sheet.autoSizeColumn(i);
        }

        response.setContentType("application/vnd.ms-excel;charset=utf-8");
        OutputStream os = response.getOutputStream();
        wb.write(os);
        os.flush();
        os.close();
    }

    /**
     * 获取样式
     * @param hssfWorkbook
     * @param styleNum
     * @return
     */
    public HSSFCellStyle getStyle(HSSFWorkbook hssfWorkbook, Integer styleNum){
        HSSFCellStyle style = hssfWorkbook.createCellStyle();
        style.setBorderRight(BorderStyle.THIN);//右边框
        style.setBorderBottom(BorderStyle.THIN);//下边框

        HSSFFont font = hssfWorkbook.createFont();
        font.setFontName("微软雅黑");//设置字体为微软雅黑

        HSSFPalette palette = hssfWorkbook.getCustomPalette();//拿到palette颜色板,可以根据需要设置颜色
        switch (styleNum){
            case(0):{
                style.setAlignment(HorizontalAlignment.CENTER_SELECTION);//跨列居中
                font.setBold(true);//粗体
                font.setFontHeightInPoints((short) 14);//字体大小
                style.setFont(font);
                palette.setColorAtIndex(HSSFColor.BLUE.index,(byte)184,(byte)204,(byte)228);//替换颜色板中的颜色
                style.setFillForegroundColor(HSSFColor.BLUE.index);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            break;
            case(1):{
                font.setBold(true);//粗体
                font.setFontHeightInPoints((short) 11);//字体大小
                style.setFont(font);
            }
            break;
            case(2):{
                font.setFontHeightInPoints((short)10);
                style.setFont(font);
            }
            break;
            case(3):{
                style.setFont(font);

                palette.setColorAtIndex(HSSFColor.GREEN.index,(byte)0,(byte)32,(byte)96);//替换颜色板中的颜色
                style.setFillForegroundColor(HSSFColor.GREEN.index);
                style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            }
            break;
        }

        return style;
    }
}
