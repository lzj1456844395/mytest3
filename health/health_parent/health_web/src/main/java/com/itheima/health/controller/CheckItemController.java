package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.service.CheckItemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/checkitem")
public class CheckItemController {

    @Reference
    private CheckItemService checkItemService;

    @GetMapping("/findAll")
    public Result findAll(){
        // 调用服务查询所有的检查项
        List<CheckItem> list = checkItemService.findAll();
        // 封装返回的结果
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
    }

    /**
     * 新增检查项
     * @param checkitem
     * @return
     */
    @PostMapping("/add")
    @PreAuthorize("hasAuthority('CHECKITEM_ADD')")
    public Result add(@RequestBody CheckItem checkitem){
        // 调用业务服务
        checkItemService.add(checkitem);
        // 响应结果给前端
        return new Result(true, MessageConstant.ADD_CHECKITEM_SUCCESS);
    }

    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    @PreAuthorize("hasAuthority('CHECKITEM_QUERY')")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用业务来分页
        PageResult<CheckItem> pageResult = checkItemService.findPage(queryPageBean);

        //return pageResult;
        // 返回给页面, 包装到Result, 统一风格
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,pageResult);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 调用业务服务
        //try {
            checkItemService.deleteById(id);
        //} catch (Exception e) {
         //   e.printStackTrace();
        //}
        // 响应结果
        return new Result(true, MessageConstant.DELETE_CHECKITEM_SUCCESS);
    }

    /**
     * 通过id查询
     */
    @GetMapping("/findById")
    public Result findById(int id){
        CheckItem checkItem = checkItemService.findById(id);
        return new Result(true, MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
    }

    /**
     * 修改检查项
     * @param checkitem
     * @return
     */
    @PostMapping("/update")
    public Result update(@RequestBody CheckItem checkitem){
        // 调用业务服务
        checkItemService.update(checkitem);
        // 响应结果给前端
        return new Result(true, MessageConstant.EDIT_CHECKITEM_SUCCESS);
    }
}
