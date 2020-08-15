package com.itheima.health.dao;

import com.itheima.health.pojo.OrderSetting;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface OrderSettingDao {
    /**
     * 通过日期来查询预约设置
     * @param orderDate
     * @return
     */
    OrderSetting findByOrderDate(Date orderDate);

    /**
     * 更新可预约数量
     * @param orderSetting
     */
    void updateNumber(OrderSetting orderSetting);

    /**
     * 添加预约设置
     * @param orderSetting
     */
    void add(OrderSetting orderSetting);

    /**
     * 通过日期范围查询预约设置信息
     * @param startDate
     * @param endDate
     * @return
     */
    List<Map<String, Integer>> getOrderSettingBetweenDate(@Param("startDate") String startDate, @Param("endDate") String endDate);

    /**
     * 更新已预约人数
     */
    void editReservationsByOrderDate(OrderSetting orderSetting);

    void deleteOrderSetting();
}
