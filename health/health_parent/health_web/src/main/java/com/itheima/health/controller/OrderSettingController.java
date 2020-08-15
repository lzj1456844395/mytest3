package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import com.itheima.health.utils.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/ordersetting")
public class OrderSettingController {

    @Reference
    private OrderSettingService orderSettingService;


    // 上传excel文件
    @PostMapping("/upload")
    public Result upload(MultipartFile excelFile){
        try {
            // 读取excel内容
            List<String[]> strings = POIUtils.readExcel(excelFile);
            // 转成List<OrderSetting>
            List<OrderSetting> orderSettingList = new ArrayList<OrderSetting>();
            // 日期格式解析
            SimpleDateFormat sdf = new SimpleDateFormat(POIUtils.DATE_FORMAT);
            Date orderDate = null;
            OrderSetting os = null;
            for (String[] dataArr : strings) {
                orderDate = sdf.parse(dataArr[0]);
                int number = Integer.valueOf(dataArr[1]);
                os = new OrderSetting(orderDate,number);
                orderSettingList.add(os);
            }
            // 调用业务服务
            orderSettingService.add(orderSettingList);
            return new Result(true, MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    @GetMapping("/getOrderSettingByMonth")
    public Result getOrderSettingByMonth(String month){
        // 调用服务查询月份数据
        List<Map<String,Integer>> monthData = orderSettingService.getOrderSettingByMonth(month);
        // 返回给前端
        return new Result(true, MessageConstant.GET_ORDERSETTING_SUCCESS,monthData);
    }

    /**
     * 基于日期的预约设置
     * @param orderSetting
     * @return
     */
    @PostMapping("/editNumberByDate")
    public Result editNumberByDate(@RequestBody OrderSetting orderSetting){
        // 基于日期的预约设置
        orderSettingService.editNumberByDate(orderSetting);
        return new Result(true, MessageConstant.ORDERSETTING_SUCCESS);
    }
}
