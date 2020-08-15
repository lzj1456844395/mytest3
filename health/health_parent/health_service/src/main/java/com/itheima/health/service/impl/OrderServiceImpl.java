package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.MemberDao;
import com.itheima.health.dao.OrderDao;
import com.itheima.health.dao.OrderSettingDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Member;
import com.itheima.health.pojo.Order;
import com.itheima.health.pojo.OrderSetting;
import com.itheima.health.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = OrderService.class)
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao orderSettingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    /**
     * 预约提交
     * @param orderInfo
     */
    @Transactional
    @Override
    public Order submit(Map<String, Object> orderInfo)  {
        //- 判断能否预约
        //  - 通过预约日期查询预约设置信息
        String orderDateStr = (String)orderInfo.get("orderDate");
        Date orderDate = null;
        try {
            orderDate = new SimpleDateFormat("yyyy-MM-dd").parse(orderDateStr);
        } catch (ParseException e) {
            e.printStackTrace();
            throw new HealthException("日期格式不正确");
        }
        OrderSetting orderSetting = orderSettingDao.findByOrderDate(orderDate);
        //  - 没有设置信息 null   报错
        if(null == orderSetting){
            throw new HealthException("选中的日期不可预约");
        }
        //  - 有设置信息
        //    - 判断number<=reservations 预约满 报错
        if(orderSetting.getReservations() >= orderSetting.getNumber()){
            throw new HealthException(MessageConstant.ORDER_FULL);
        }
        //    - 未满 可预约 update reservations+1
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        //
        String telephone = ((String) orderInfo.get("telephone"));
        //- 判断是否为会员, 通过手机查询
        Member member = memberDao.findByTelephone(telephone);
        Order order = new Order();
        //      通过预约日期，套餐id, 会员id
        order.setSetmealId(Integer.valueOf((String) orderInfo.get("setmealId")));
        order.setOrderDate(orderDate);
        if(null == member){
            //  - 不存在， 添加新会员 <selectKey>
            member = new Member();
            member.setRegTime(new Date());
            member.setSex(((String) orderInfo.get("sex")));
            member.setPhoneNumber(telephone);
            member.setIdCard(((String) orderInfo.get("idCard")));
            member.setName(((String) orderInfo.get("name")));
            memberDao.add(member);
            // 为添加订单
            order.setMemberId(member.getId());
        }else {
            // 为了查询
            order.setMemberId(member.getId());
            // 会员存在 判断重复预约
            List<Order> orderList = orderDao.findByCondition(order);
            //      存在报错
            if (null != orderList && orderList.size() > 0) {
                throw new HealthException(MessageConstant.HAS_ORDERED);
            }
        }
        //- 添加订单
        order.setOrderType(((String) orderInfo.get("orderType")));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        orderDao.add(order);
        return order;
    }

    /**
     * 通过订单id查询预约信息
     * @param id
     * @return
     */
    @Override
    public Map<String, Object> findOrderDetailById(int id) {
        return orderDao.findById4Detail(id);
    }
}
