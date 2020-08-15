package com.itheima.health.exception;

/**
 * Description: 自定义异常
 * 友好提示
 * 终止已经不符合业务逻辑的代码
 * User: Eric
 */
public class HealthException extends RuntimeException{
    public HealthException(String message){
        super(message);
    }
}
