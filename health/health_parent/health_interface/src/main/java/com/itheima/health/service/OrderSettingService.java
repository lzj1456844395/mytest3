package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;

import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface OrderSettingService {
    /**
     * 批量导入
     * @param orderSettingList
     */
    void add(List<OrderSetting> orderSettingList) throws HealthException;

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    List<Map<String, Integer>> getOrderSettingByMonth(String month);

    /**
     *  基于日期的预约设置
     * @param orderSetting
     */
    void editNumberByDate(OrderSetting orderSetting) throws HealthException;
    /**
     * 定期清理预约数据表
     */
    void deleteOrderSetting();
}
