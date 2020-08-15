package com.itheima.health.job;

import com.itheima.health.constant.RedisConstant;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

/**
 * Description: 清理垃圾图片 任务类
 * User: Eric
 *
 * Component   通过注解方法注册到spring容器中，如果没指定name属性，则默认用类名 首字母小写
 * Controller
 * Service
 * Repository
 */
@Component
public class ClearImgJob {

    @Autowired
//    private JedisPool jedisPool;

    /**
     * 清理垃圾图片的执行方法
     */
    public void clearImg(){
        // 获取 redis的连接
//        Jedis jedis = jedisPool.getResource();
        // 计算2个set集合的差集 所有七牛图片-保存到数据库
        // 需要删除的图片
//        Set<String> need2Delete = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        // 调用七牛删除
//        QiNiuUtils.removeFiles(need2Delete.toArray(new String[]{}));
        // 删除redis上的图片, 两边的图片已经同步了
//        jedis.del(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
    }
}
