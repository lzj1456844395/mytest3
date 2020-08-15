package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Role;

import java.util.List;
import java.util.Map;

public interface RoleService {

    Map<String,Object> findMenuAndPermission();

    void add(Object[] arr);

    Role findById(Integer id);

    void update(Object[] arr);

    void delete(Integer id)throws HealthException;

    PageResult<Role> findPage(QueryPageBean queryPageBean);
}
