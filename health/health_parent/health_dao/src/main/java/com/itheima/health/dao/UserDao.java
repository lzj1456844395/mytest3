package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.User;
import org.apache.ibatis.annotations.Param;

/**
 * Description: No Description
 * User: Eric
 */
public interface UserDao {
    /**
     * 根据登陆用户名称查询用户权限信息
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    void add(User user);

    void addRoleRelation(@Param("id") Integer id, @Param("rolesId") Integer rolesId);

    Page<User> findCondition(String queryString);

    void deleteCondition(Integer id);

    void deleteUser(Integer id);

    User findById(Integer id);

    void update(User user);
}
