package com.itheima.health.job;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.service.OrderSettingService;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

/**
 * @Author Tian Qing
 * @Daate: Created in 21:23 2020/7/5
 */
@Component
public class ClearOrderSettingJob {
//    @Autowired(required = false)
//    @Qualifier("OrderSettingServiceImpl")

    @Reference
    private OrderSettingService orderSettingService;

    /**
     * 在每个月的最后一天的凌晨2点清理
     * 定期清理已经过期的预约数据
     */
    public void clearOrderSetting() {
        System.out.println("执行清理");
        orderSettingService.deleteOrderSetting();
    }

}
