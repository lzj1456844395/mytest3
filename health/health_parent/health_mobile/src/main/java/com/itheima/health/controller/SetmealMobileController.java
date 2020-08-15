package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealMobileController {

    @Reference
    private SetmealService setmealService;

    /**
     * 查询所有
     */
    @GetMapping("/getSetmeal")
    public Result getSetmeal(){
        // 查询所有的套餐
        List<Setmeal> list = setmealService.findAll();
        // 套餐里有图片有全路径吗? 拼接全路径
        list.forEach(s->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        return new Result(true, MessageConstant.GET_SETMEAL_LIST_SUCCESS,list);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findById(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findDetailById")
    public Result findDetailById(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findDetailById(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findDetailById2")
    public Result findDetailById2(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findDetailById2(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }

    /**
     * 查询套餐详情
     */
    @GetMapping("/findDetailById3")
    public Result findDetailById3(int id){
        // 调用服务查询详情
        Setmeal setmeal = setmealService.findDetailById3(id);
        // 设置图片的完整路径
        setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,setmeal);
    }
}
