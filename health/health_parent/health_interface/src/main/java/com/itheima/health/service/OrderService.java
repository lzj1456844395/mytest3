package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Order;

import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface OrderService {
    /**
     * 提交预约
     * @param orderInfo
     */
    Order submit(Map<String, Object> orderInfo) throws HealthException;

    /**
     * 通过订单id查询预约信息
     * @param id
     * @return
     */
    Map<String, Object> findOrderDetailById(int id);
}
