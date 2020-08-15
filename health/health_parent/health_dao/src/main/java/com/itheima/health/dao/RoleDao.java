package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.Role;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface RoleDao {

    void add(Role role);

    void addRoleAndMenu(@Param("rid") Integer rid, @Param("mid") Integer mid);

    void addRoleAndPermission(@Param("rid") Integer rid, @Param("pid") Integer pid);

    Role findById(Integer id);

    void update(Role role);

    void deleteRoleAndMenuByRoleId(Integer id);

    void deleteRoleAndPermissionByRoleId(Integer id);

    Integer relevance(Integer id);

    void delete(Integer id);

    Page<Role> findPage(String queryString);
}
