package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/menu")
public class MenuController {
    @Reference
    private MenuService menuService;

    @GetMapping("/getmenu")
    public Result getmenu() {
        //获取用户名
        org.springframework.security.core.userdetails.User loginUser = (org.springframework.security.core.userdetails.User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = loginUser.getUsername();
        //根据用户查找相对应菜单
        List<Menu> resultList=menuService.findMenuByName(username);
        //返回数据
        return new Result(true, MessageConstant.GET_MENU_SUCCESS,resultList);
    }

    @GetMapping("/findoneMenuList")
    public Result findoneMenuList(){
        List<Menu> list=menuService.findoneMenuList();
        return new Result(true, "菜单查询成功",list);
    }
    @GetMapping("/findoneMenu")
    public Result findoneMenu(Integer id){
        Map<String, Object> map=menuService.findoneMenu(id);
        return new Result(true, "菜单回显成功",map);
    }
    @PostMapping("/Edit")
    public Result edit(@RequestBody Map<String,Object> map){
        menuService.edit(map);
        return new Result(true, "编辑菜单成功");
    }
    @PostMapping("/delete")
    public Result delete(Integer id){
        menuService.delete(id);
        return new Result(true, "删除菜单成功");
    }
    @GetMapping("/getfucaidan")
    public Result getfucaidan(){
        List<Menu> menu=menuService.getfucaidan();
        return new Result(true, "回显一级菜单成功",menu);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Menu menu){
        menuService.add(menu);
        return new Result(true, "菜单添加成功");
    }
}
