package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.service.ReportService;
import com.itheima.health.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = ReportService.class)
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReport() {
        Map<String, Object> reportData = new HashMap<String,Object>();
        //reportDate
        Date today = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        // 星期一
        String monday = sdf.format(DateUtils.getFirstDayOfWeek(today));
        // 星期天
        String sunday = sdf.format(DateUtils.getLastDayOfWeek(today));
        // 1号
        String firstDayOfThisMonth = sdf.format(DateUtils.getFirstDay4ThisMonth());
        // 本月最后一天
        String lastDayOfThisMonth = sdf.format(DateUtils.getLastDayOfThisMonth());


        String reportDate = sdf.format(today);
        //=======================会员数量===========================
        //todayNewMember 今日新增会员
        int todayNewMember = memberDao.findMemberCountByDate(reportDate);
        //totalMember
        int totalMember = memberDao.findMemberTotalCount();
        //thisWeekNewMember 本周新增会员数
        int thisWeekNewMember = memberDao.findMemberCountAfterDate(monday);
        //thisMonthNewMember 本月新增会员数
        int thisMonthNewMember = memberDao.findMemberCountAfterDate(firstDayOfThisMonth);
        //==================================================

        //========================订单统计============================
        //todayOrderNumber 今日预约数
        int todayOrderNumber = orderDao.findOrderCountByDate(reportDate);
        //todayVisitsNumber 今日到诊数
        int todayVisitsNumber = orderDao.findVisitsCountByDate(reportDate);
        //thisWeekOrderNumber 本周预约数
        int thisWeekOrderNumber = orderDao.findOrderCountBetweenDate(monday, sunday);
        //thisWeekVisitsNumber 本周到诊数
        int thisWeekVisitsNumber = orderDao.findVisitsCountAfterDate(monday);
        //thisMonthOrderNumber 本月预约数
        int thisMonthOrderNumber = orderDao.findOrderCountBetweenDate(firstDayOfThisMonth, lastDayOfThisMonth);
        //thisMonthVisitsNumber 本月到诊数
        int thisMonthVisitsNumber = orderDao.findVisitsCountAfterDate(firstDayOfThisMonth);

        //========================== 热门套餐========================
        //hotSetmeal
        List<Map<String,Object>> hotSetmeal = orderDao.findHotSetmeal();

        reportData.put("reportDate",reportDate);
        reportData.put("todayNewMember",todayNewMember);
        reportData.put("totalMember",totalMember);
        reportData.put("thisWeekNewMember",thisWeekNewMember);
        reportData.put("thisMonthNewMember",thisMonthNewMember);
        reportData.put("todayOrderNumber",todayOrderNumber);
        reportData.put("todayVisitsNumber",todayVisitsNumber);
        reportData.put("thisWeekOrderNumber",thisWeekOrderNumber);
        reportData.put("thisWeekVisitsNumber",thisWeekVisitsNumber);
        reportData.put("thisMonthOrderNumber",thisMonthOrderNumber);
        reportData.put("thisMonthVisitsNumber",thisMonthVisitsNumber);
        reportData.put("hotSetmeal",hotSetmeal);

        return reportData;
    }

    @Override
    public List<Integer> getMemberReportPro(List<String> arrx) {
        List<Integer> arry = new ArrayList<>();
        Integer num=null;
        for (int i = 0; i < arrx.size()-2; i++) {
           num= memberDao.findMemberCountBeforeDate1(arrx.get(i+1)+"-01");
           arry.add(num);
        }
        //判断最后一个日期格式是 2020-07 || 2020-07-06
        String s = arrx.get(arrx.size() - 1);
        if (s.length()>7) {
            num= memberDao.findMemberCountBeforeDate1(arrx.get(arrx.size() - 1)+"-01");
            arry.add(num);
            num= memberDao.findMemberCountBeforeDate1(s);
            arry.add(num);
        }else {
            num= memberDao.findMemberCountBeforeDate1(s+"-01");
            arry.add(num);
        }


        return arry;
    }

    @Override
    public Map<String, Object> getMemberPart() {

        HashMap<String, Object> map = new HashMap<>();

        HashMap<String, Object> sexMap = new HashMap<>();
        HashMap<String, Object> ageMap = new HashMap<>();
/*===========================================性别=================================================*/
        // 查询性别分组
        List<Map<String,Object>> sexList = memberDao.getSexPart();
        List<String> sexNameList = new ArrayList<>();
        if (null != sexList) {
            for (Map<String, Object> m : sexList) {
                String sexName = (String) m.get("name");
                if ("1".equals(sexName)) {
                    m.put("name", "男");
                } else if ("2".equals(sexName)) {
                    m.put("name", "女");
                }
                sexNameList.add((String) m.get("name"));
            }
        }

        sexMap.put("sexNameList", sexNameList);
        sexMap.put("sexList", sexList);
        map.put("sexMap", sexMap);

/*===========================================年龄=================================================*/
        // 获取每个年龄对应的出生日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar calendar = Calendar.getInstance();
        // 18岁的出生日期
        calendar.add(Calendar.YEAR, -18);
        String age18 = sdf.format(calendar.getTime());
        // 30岁的出生日期
        calendar.add(Calendar.YEAR, -12);
        String age30 = sdf.format(calendar.getTime());
        // 45岁的出生日期
        calendar.add(Calendar.YEAR, -15);
        String age45 = sdf.format(calendar.getTime());

        List<String> ageNameList = new ArrayList<>();

        HashMap<String, Object> countMap = null;
        ArrayList<Map<String,Object>> ageList = new ArrayList<>();
        // 0-18岁
        Integer count18 = memberDao.findCountByAge(null, age18);
        if (count18 != 0) {
            countMap = new HashMap<>();
            countMap.put("name", "0-18");
            countMap.put("value", count18);
            ageList.add(countMap);
            ageNameList.add("0-18");
        }

        // 18-30岁
        Integer count30 = memberDao.findCountByAge(age18,age30);
        if (count30 != 0) {
            countMap = new HashMap<>();
            countMap.put("name", "18-30");
            countMap.put("value", count30);
            ageList.add(countMap);
            ageNameList.add("18-30");
        }

        // 30-45岁
        Integer count45 = memberDao.findCountByAge(age30,age45);
        if (count45 != 0) {
            countMap = new HashMap<>();
            countMap.put("name", "30-45");
            countMap.put("value", count45);
            ageList.add(countMap);
            ageNameList.add("30-45");
        }

        // >45岁
        Integer countMore45 = memberDao.findCountByAge(age45, null);
        if (countMore45 != 0) {
            countMap = new HashMap<>();
            countMap.put("name", ">45");
            countMap.put("value", count18);
            ageList.add(countMap);
            ageNameList.add(">45");
        }

        // 年龄未知
        Integer countNull = memberDao.findCountByAge(null, null);
        if (countNull != 0) {
            countMap = new HashMap<>();
            countMap.put("name", "未知");
            countMap.put("value", countNull);
            ageList.add(countMap);
            ageNameList.add("未知");
        }

        ageMap.put("ageList", ageList);
        ageMap.put("ageNameList", ageNameList);
/*=============================================================================*/
        map.put("ageMap", ageMap);
        return map;
    }
}
