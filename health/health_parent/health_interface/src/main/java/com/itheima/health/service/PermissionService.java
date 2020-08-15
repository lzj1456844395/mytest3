package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface PermissionService {


    void add(Permission permission);

    List<Permission> findAll();

    Permission findById(Integer id);

    void edit(Permission permission);

    void deleteById(Integer id);
}
