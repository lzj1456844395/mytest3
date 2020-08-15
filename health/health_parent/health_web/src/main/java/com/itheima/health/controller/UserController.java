package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/user")
public class UserController {
    @Reference
    private UserService userService;


    /**
     * 获取登陆用户名
     */
    @GetMapping("/getLoginUsername")
    public Result getLoginUsername() {
        // 获取登陆用户的认证信息
        User loginUser = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        // 登陆用户名
        String username = loginUser.getUsername();
        // 返回给前端
        return new Result(true, MessageConstant.GET_USERNAME_SUCCESS, username);
    }

    /**
     * 新增用户
     *
     * @param user
     * @return
     */
    @PostMapping("/add")
    public Result add(@RequestBody User user, Integer[] roleIds) {
        if (user.getUsername() == null) {
            return new Result(false, "用户名不能为空");
        }
        if (user.getPassword() == null || user.getPassword().length() == 0) {
            user.setPassword("1234");
        }
        userService.add(user, roleIds);
        return new Result(true, MessageConstant.ADD_USER_SUCCESS);
    }

    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean) {
        PageResult<User> pageResult = userService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS, pageResult);
    }

    @GetMapping("/delete")
    public Result delete(Integer id) {
        userService.delete(id);
        return new Result(true, MessageConstant.DELETE_USER_SUCCESS);
    }

    @RequestMapping("findById")
    public Result findById(Integer id) {
        User user = userService.findById(id);
        return new Result(true, "", user);
    }

    @PostMapping("/update")
    public Result update(@RequestBody User user,Integer[] roleIds){
        userService.update(user,roleIds);
        return new Result(true,MessageConstant.UPDATE_USER_SUCCESS);
    }
}
