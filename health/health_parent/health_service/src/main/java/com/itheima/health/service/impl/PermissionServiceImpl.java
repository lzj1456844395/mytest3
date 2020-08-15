package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.CheckItemDao;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Permission;
import com.itheima.health.service.CheckItemService;
import com.itheima.health.service.PermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * Description: No Description
 * 解决 dubbo 2.6.0 【注意，注意，注意】
 * interfaceClass 发布出去服务的接口为这个CheckItemService.class
 * 没加interfaceClass, 调用No Provider 的异常
 * User: Eric
 */
@Service(interfaceClass = PermissionService.class)
public class PermissionServiceImpl implements PermissionService {

    @Autowired
    PermissionDao permissionDao;

    @Override
    public void add(Permission permission) {
        permissionDao.add(permission);
    }

    @Override
    public List< Permission> findAll() {
        return permissionDao.findAll();
    }

    @Override
    public Permission findById(Integer id) {
        return permissionDao.findById(id);
    }

    @Override
    public void edit(Permission permission) {
        permissionDao.edit(permission);
    }

    @Override
    public void deleteById(Integer id) {
        //查询permission在t_role_permission中被使用的总次数
        int count = permissionDao.findCountByPermissionId(id);
        //判断是否被使用
        if(count != 0){
            throw new HealthException(MessageConstant.PERMISSION_IN_USE);
        }
        permissionDao.deleteById(id);
    }


}
