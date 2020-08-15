package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = OrderSettingService.class)
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Override
    @Transactional
    public void add(List<OrderSetting> orderSettingList) {
        // 遍历
        for (OrderSetting orderSetting : orderSettingList) {
            // 判断是否存在, 通过日期来查询, 注意：日期里是否有时分秒，数据库里的日期是没有时分秒的
            OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
            if(null != osInDB){
                // 数据库存在这个预约设置, 已预约数量不能大于可预约数量
                if(osInDB.getReservations() > orderSetting.getNumber()){
                    throw new HealthException(orderSetting.getOrderDate() + " 中已预约数量不能大于可预约数量");
                }
                orderSettingDao.updateNumber(orderSetting);
            }else{
                // 不存在
                orderSettingDao.add(orderSetting);
            }
        }
    }

    /**
     * 通过月份查询预约设置信息
     * @param month
     * @return
     */
    @Override
    public List<Map<String, Integer>> getOrderSettingByMonth(String month) {
        String startDate = month + "-01";
        String endDate = month + "-31";
        return orderSettingDao.getOrderSettingBetweenDate(startDate, endDate);
    }

    /**
     *  基于日期的预约设置
     * @param orderSetting
     */
    @Override
    public void editNumberByDate(OrderSetting orderSetting) {
        // 判断是否存在, 通过日期来查询, 注意：日期里是否有时分秒，数据库里的日期是没有时分秒的
        OrderSetting osInDB = orderSettingDao.findByOrderDate(orderSetting.getOrderDate());
        if(null != osInDB){
            // 数据库存在这个预约设置, 已预约数量不能大于可预约数量
            if(osInDB.getReservations() > orderSetting.getNumber()){
                throw new HealthException(orderSetting.getOrderDate() + " 中已预约数量不能大于可预约数量");
            }
            orderSettingDao.updateNumber(orderSetting);
        }else{
            // 不存在
            orderSettingDao.add(orderSetting);
        }
    }

    @Override
    public void deleteOrderSetting() {
        orderSettingDao.deleteOrderSetting();
    }
}
