package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.MemberService;
import com.itheima.health.service.ReportService;
import com.itheima.health.service.SetmealService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jxls.common.Context;
import org.jxls.transform.poi.PoiContext;
import org.jxls.util.JxlsHelper;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    /**
     * 会员拆线图
     * @return
     */
    @GetMapping("/getMemberReport")
    public Result getMemberReport(){
        // 组装过去12个月的数据, 前端是一个数组
        List<String> months = new ArrayList<String>();
        // 使用java中的calendar来操作日期, 日历对象
        Calendar calendar = Calendar.getInstance();
        // 设置过去一年的时间  年-月-日 时:分:秒, 减去12个月
        calendar.add(Calendar.MONTH, -12);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        // 构建12个月的数据
        for (int i = 0; i < 12; i++) {
            // 每次增加一个月就
            calendar.add(Calendar.MONTH, 1);
            // 过去的日期, 设置好的日期
            Date date = calendar.getTime();
            // 2020-06 前端只展示年-月
            months.add(sdf.format(date));
        }

        // 调用服务查询过去12个月会员数据 前端也是一数组 数值
        List<Integer> memberCount =memberService.getMemberReport(months);
        // 放入map
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("months",months);
        resultMap.put("memberCount",memberCount);

        // 再返回给前端
        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_SUCCESS,resultMap);
    }
    /**
     * 会员拆线图增强版
     * @return
     */
    @PostMapping("/getMemberReportPro")
    public Result getMemberReportPro(@RequestBody Date[] date){
        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
        //起始日期
        Date startDate = date[0];
        //结束日期
        Date endDate = date[1];
        //日历的日期不能大于实际时间
        Date date1 = new Date();
        if (startDate.getTime()>date1.getTime()){
            try {
                startDate=sdf2.parse(sdf2.format(date1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        if (endDate.getTime()>date1.getTime()){
            try {
                endDate=sdf2.parse(sdf2.format(date1));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        //--------组装x轴的日期-------------
        List<String> arrx =new ArrayList<>();//结果集合
        Calendar startCa = Calendar.getInstance();
        startCa.setTime(startDate);
        Calendar endCa = Calendar.getInstance();
        endCa.setTime(endDate);
        //添加二个时间之间所有月份
        if (startCa.get(Calendar.DAY_OF_MONTH)!=1) {//判断该日期是否是该月的第一天
            arrx.add(sdf2.format(startCa.getTime()));
            startCa.add(Calendar.MONTH, 1);
        }
        while (startCa.getTime().getTime()<=endDate.getTime()){
            arrx.add(sdf1.format(startCa.getTime()));
            startCa.add(Calendar.MONTH, 1);
        }
        //判断该日期是否是该月的最后一天
        if (endCa.get(Calendar.DAY_OF_MONTH) != endCa.getActualMaximum(Calendar.DAY_OF_MONTH)){
            arrx.add(sdf2.format(endCa.getTime()));
        }else {
            arrx.add(sdf1.format(endCa.getTime()));
        }
        //----------------end--------------------

       //--------数据库查询y轴信息-------------
        List<Integer> arry=reportService.getMemberReportPro(arrx);
        //y轴不是具体时间日期的拼接'月'字
        for (int i = 0; i < arrx.size(); i++) {
            if (arrx.get(i).length()<=7) {
                arrx.set(i, arrx.get(i)+"月");
            }
        }
        Map<String, Object> map = new HashMap<>();
        map.put("arrx", arrx);
        map.put("arry", arry);


        return new Result(true, MessageConstant.GET_MEMBER_NUMBER_REPORT_FAIL,map);
    }


    /**
     * 套餐预约占比
     */
    @GetMapping("/getSetmealReport")
    public Result getSetmealReport(){
        // 调用服务查询
        // 套餐数量
        List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
        // 套餐名称集合
        List<String> setmealNames = new ArrayList<String>();
        // [{name:,value}]
        // 抽取套餐名称
        if(null != setmealCount){
            for (Map<String, Object> map : setmealCount) {
                //map {name:,value}
                // 获取套餐的名称
                setmealNames.add((String) map.get("name"));
            }
        }
        // 封装返回的结果
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("setmealNames",setmealNames);
        resultMap.put("setmealCount",setmealCount);

        return new Result(true, MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,resultMap);
    }

    /**
     * 运营统计数据
     * @return
     */
    @GetMapping("/getBusinessReportData")
    public Result getBusinessReportData() {
        Map<String, Object> businessReport = reportService.getBusinessReport();
        return new Result(true, MessageConstant.GET_BUSINESS_REPORT_SUCCESS,businessReport);

    }

    /**
     * 导出运营统计数据报表
     */
    @GetMapping("/exportBusinessReport")
    public void exportBusinessReport(HttpServletRequest req, HttpServletResponse res){
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String template = req.getSession().getServletContext().getRealPath("/template/report_template.xlsx");
        // 创建工作簿(模板路径)
        try (// 写在try()里的对象，必须实现closable接口，try()cathc()中的finally
            OutputStream os = res.getOutputStream();
            XSSFWorkbook wk = new XSSFWorkbook(template);){
            // 获取工作表
            XSSFSheet sht = wk.getSheetAt(0);
            // 获取运营统计数据
            Map<String, Object> reportData = reportService.getBusinessReport();
            // 日期 坐标 2,5
            sht.getRow(2).getCell(5).setCellValue(reportData.get("reportDate").toString());
            //======================== 会员 ===========================
            // 新增会员数 4,5
            sht.getRow(4).getCell(5).setCellValue((Integer)reportData.get("todayNewMember"));
            // 总会员数 4,7
            sht.getRow(4).getCell(7).setCellValue((Integer)reportData.get("totalMember"));
            // 本周新增会员数5,5
            sht.getRow(5).getCell(5).setCellValue((Integer)reportData.get("thisWeekNewMember"));
            // 本月新增会员数 5,7
            sht.getRow(5).getCell(7).setCellValue((Integer)reportData.get("thisMonthNewMember"));

            //=================== 预约 ============================
            sht.getRow(7).getCell(5).setCellValue((Integer)reportData.get("todayOrderNumber"));
            sht.getRow(7).getCell(7).setCellValue((Integer)reportData.get("todayVisitsNumber"));
            sht.getRow(8).getCell(5).setCellValue((Integer)reportData.get("thisWeekOrderNumber"));
            sht.getRow(8).getCell(7).setCellValue((Integer)reportData.get("thisWeekVisitsNumber"));
            sht.getRow(9).getCell(5).setCellValue((Integer)reportData.get("thisMonthOrderNumber"));
            sht.getRow(9).getCell(7).setCellValue((Integer)reportData.get("thisMonthVisitsNumber"));

            // 热门套餐
            List<Map<String,Object>> hotSetmeal = (List<Map<String,Object>> )reportData.get("hotSetmeal");
            int row = 12;
            for (Map<String, Object> setmealMap : hotSetmeal) {
                sht.getRow(row).getCell(4).setCellValue((String)setmealMap.get("name"));
                sht.getRow(row).getCell(5).setCellValue((Long)setmealMap.get("setmeal_count"));
                BigDecimal proportion = (BigDecimal) setmealMap.get("proportion");
                sht.getRow(row).getCell(6).setCellValue(proportion.doubleValue());
                sht.getRow(row).getCell(7).setCellValue((String)setmealMap.get("remark"));
                row++;
            }

            // 工作簿写给reponse输出流
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            res.setHeader("Content-Disposition","attachement;filename=" + filename);
            wk.write(os);
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出运营统计数据报表
     */
    @GetMapping("/exportBusinessReport2")
    public void exportBusinessReport2(HttpServletRequest req, HttpServletResponse res){
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String template = req.getSession().getServletContext().getRealPath("/template/report_template2.xlsx");
        // 创建工作簿(模板路径)
        try (// 写在try()里的对象，必须实现closable接口，try()cathc()中的finally
             OutputStream os = res.getOutputStream();
            ){
            // 获取运营统计数据
            Map<String, Object> reportData = reportService.getBusinessReport();
            // 数据模型
            Context context = new PoiContext();
            context.putVar("obj", reportData);


            // 工作簿写给reponse输出流
            res.setContentType("application/vnd.ms-excel");
            String filename = "运营统计数据报表.xlsx";
            filename = new String(filename.getBytes(), "ISO-8859-1");
            // 设置头信息，告诉浏览器，是带附件的，文件下载
            res.setHeader("Content-Disposition","attachement;filename=" + filename);
            // 把数据模型中的数据填充到文件中
            JxlsHelper.getInstance().processTemplate(new FileInputStream(template),os,context);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 导出运营统计数据报表
     */
    @GetMapping("/exportBusinessReportPdf")
    public Result exportBusinessReportPdf(HttpServletRequest req, HttpServletResponse res){
        // 获取模板的路径, getRealPath("/") 相当于到webapp目录下
        String basePath = req.getSession().getServletContext().getRealPath("/template");
        // jrxml路径
        String jrxml = basePath + File.separator + "report_business.jrxml";
        // jasper路径
        String jasper = basePath + File.separator + "report_business.jasper";

        try {
            // 编译模板
            JasperCompileManager.compileReportToFile(jrxml, jasper);
            Map<String, Object> businessReport = reportService.getBusinessReport();
            // 热门套餐(list -> Detail1)
            List<Map<String,Object>> hotSetmeals = (List<Map<String,Object>>)businessReport.get("hotSetmeal");
            // 填充数据 JRBeanCollectionDataSource自定数据
            JasperPrint jasperPrint = JasperFillManager.fillReport(jasper, businessReport, new JRBeanCollectionDataSource(hotSetmeals));
            res.setContentType("application/pdf");
            res.setHeader("Content-Disposition","attachement;filename=businessReport.pdf");
            JasperExportManager.exportReportToPdfStream(jasperPrint, res.getOutputStream());
            return null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new Result(false,"导出运营数据统计pdf失败");
    }

    @GetMapping("getMemberPart")
    public Result getMemberPart() {
        Map<String,Object> map = reportService.getMemberPart();
        return new Result(true, MessageConstant.GET_MEMBER_PART_REPORT_SUCCESS, map);
    }


}
