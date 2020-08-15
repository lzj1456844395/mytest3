package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface PermissionDao {


    void add(Permission permission);

    List< Permission> findAll();

    Permission findById(Integer id);

    void edit(Permission permission);

    int findCountByPermissionId(Integer id);

    void deleteById(Integer id);
}
