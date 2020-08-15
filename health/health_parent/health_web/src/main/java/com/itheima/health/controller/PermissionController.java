package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.PermissionService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Reference
    private PermissionService permissionService;

    //新增
    @RequestMapping("/add")
    public Result add(@RequestBody Permission permission) {
        // 调用业务服务
        permissionService.add(permission);
        // 响应结果给前端
        return new Result(true, MessageConstant.ADD_PERMISSION_SUCCESS);
    }

    //查询全部
    @GetMapping("/findAll")
    public Result findAll() {
        // 调用服务查询所有的权限
        List<Permission> list = permissionService.findAll();
        if (list != null && list.size() > 0) {
            // 封装返回的结果
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, list);
        }
        // 封装返回的结果
        return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
    }

    //根据id查找
    @GetMapping("/findById")
    public Result findById(Integer id) {
        // 调用服务根据查询权限
        Permission permission = permissionService.findById(id);
        if (permission != null) {
            // 封装返回的结果
            return new Result(true, MessageConstant.QUERY_PERMISSION_SUCCESS, permission);
        }
        // 封装返回的结果
        return new Result(false, MessageConstant.QUERY_PERMISSION_FAIL);
    }

    //编辑
    @RequestMapping("/edit")
    public Result edit(@RequestBody Permission permission) {

        try {
            permissionService.edit(permission);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.EDIT_PERMISSION_FAIL);
        }
        return new Result(true, MessageConstant.EDIT_PERMISSION_SUCCESS);
    }

    //根据id删除
    @RequestMapping("/deleteById")
    public Result deleteById(Integer id) {

        permissionService.deleteById(id);
        return new Result(true, MessageConstant.DELETE_PERMISSION_SUCCESS);
    }
}
