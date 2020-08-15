package com.itheima.health.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisMessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.utils.SMSUtils;
import com.itheima.health.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/validateCode")
public class ValidateCodeController {

    @Autowired
    private JedisPool jedisPool;

    /**
     * 发送手机验证码
     *
     * @param telephone
     * @return
     */
    @PostMapping("/send4Order")
    public Result send4Order(String telephone) {
        //- 生成验证码之前要检查一下是否发送过了, 通过redis获取key为手机号码，看是否存在
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_ORDER + "_" + telephone;
        // redis中的验证码
        String codeInRedis = jedis.get(key);
        if (null == codeInRedis) {
            //- 不存在，没发送，生成验证码，调用SMSUtils.发送验证码，把验证码存入redis(5,10,15分钟有效期)，value:验证码, key:手机号码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                // 发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                // 存入redis，有效时间为15分钟
                jedis.setex(key, 15*60, code + "");
                // 返回成功
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                // 发送失败
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //- 存在：验证码已经发送了，请注意查收
        return new Result(false, MessageConstant.SENT_VALIDATECODE);
    }

    /**
     * 发送登陆手机验证码
     *
     * @param telephone
     * @return
     */
    @PostMapping("/send4Login")
    public Result send4Login(String telephone) {
        //- 生成验证码之前要检查一下是否发送过了, 通过redis获取key为手机号码，看是否存在
        Jedis jedis = jedisPool.getResource();
        String key = RedisMessageConstant.SENDTYPE_LOGIN + "_" + telephone;
        // redis中的验证码
        String codeInRedis = jedis.get(key);
        if (null == codeInRedis) {
            //- 不存在，没发送，生成验证码，调用SMSUtils.发送验证码，把验证码存入redis(5,10,15分钟有效期)，value:验证码, key:手机号码
            Integer code = ValidateCodeUtils.generateValidateCode(6);
            try {
                // 发送验证码
                SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE, telephone, code + "");
                // 存入redis，有效时间为15分钟
                jedis.setex(key, 15*60, code + "");
                // 返回成功
                return new Result(true, MessageConstant.SEND_VALIDATECODE_SUCCESS);
            } catch (ClientException e) {
                e.printStackTrace();
                // 发送失败
                return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
            }
        }
        //- 存在：验证码已经发送了，请注意查收
        return new Result(false, MessageConstant.SENT_VALIDATECODE);
    }

}
