package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/role")
public class RoleController {
    @Reference
    private RoleService roleService;

    @GetMapping("/findMenuAndPermission")
    public Result findMenuAndPermission(){
        Map<String,Object> map=roleService.findMenuAndPermission();
        return new Result(true,"回显表单成功",map);
    }
    @PostMapping("/add")
    public Result add(@RequestBody Object[] arr){
        roleService.add(arr);
        return new Result(true, "角色添加成功");
    }
    @GetMapping("/findById")
    public Result findById(Integer id){
        Role role=roleService.findById(id);
        return new Result(true, "编辑回显成功",role);
    }
    @PostMapping("/update")
    public Result update(@RequestBody Object[] arr){
        roleService.update(arr);
        return new Result(true, "编辑成功");
    }
    @PostMapping("/delete")
    public Result delete(Integer id){
        roleService.delete(id);
        return new Result(true, "删除成功");
    }
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        PageResult<Role> page=roleService.findPage(queryPageBean);
        return new Result(true, "查询成功",page);
    }
}

