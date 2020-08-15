package com.itheima.health.controller;

import com.itheima.health.entity.Result;
import com.itheima.health.exception.HealthException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Description: No Description
 * User: Eric
 */
// 与前端约定好的，返回的都是json数据
@RestControllerAdvice
public class HealExceptionAdvice {

    /**
     * 自定义招出的异常处理
     * @param he
     * @return
     */
    @ExceptionHandler(HealthException.class)
    public Result handleHealthException(HealthException he){
        return new Result(false, he.getMessage());
    }

    /**
     * 所有未知的异常
     * @param he
     * @return
     */
    @ExceptionHandler(Exception.class)
    public Result handleException(Exception he){
        he.printStackTrace();
        return new Result(false, "发生未知错误，操作失败，请联系管理员");
    }

    /**
     * 密码错误
     * @param he
     * @return
     */
    @ExceptionHandler(BadCredentialsException.class)
    public Result handBadCredentialsException(BadCredentialsException he){
        return handleUserPassword();
    }

    /**
     * 用户名不存在
     * @param he
     * @return
     */
    @ExceptionHandler(InternalAuthenticationServiceException.class)
    public Result handInternalAuthenticationServiceException(InternalAuthenticationServiceException he){
        return handleUserPassword();
    }

    private Result handleUserPassword(){
        return new Result(false, "用户名或密码错误");
    }

    /**
     * 用户名不存在
     * @param he
     * @return
     */
    @ExceptionHandler(AccessDeniedException.class)
    public Result handAccessDeniedException(AccessDeniedException he){
        return new Result(false, "没有权限");
    }
}
