package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.dao.UserDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.pojo.User;
import com.itheima.health.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = UserService.class)
public class UserServiceImpl implements UserService {

    @Autowired
    private UserDao userDao;

    /**
     * 根据登陆用户名称查询用户权限信息
     *
     * @param username
     * @return
     */
    @Override
    public User findByUsername(String username) {
        return userDao.findByUsername(username);
    }

    /**
     * 新增用户
     *
     * @param user
     * @param roleIds
     */
    @Override
    @Transactional
    public void add(User user, Integer[] roleIds) {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userDao.add(user);
        if (roleIds.length > 0 && roleIds != null) {
            for (Integer roleId : roleIds) {
                userDao.addRoleRelation(user.getId(), roleId);
            }
        }
    }

    /**
     * 分页查询
     *
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<User> findPage(QueryPageBean queryPageBean) {
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //带有查询条件的进行模糊查询 如果不为空擦拼接 不模糊查询了 摊牌了没这个能力
//        if (!StringUtils.isEmpty(queryPageBean.getQueryString())) {
//            //拼接并进行模糊查询
//            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
//        }
        //调用suerDao进行关系查询返回一个page
        Page<User> page = userDao.findCondition(queryPageBean.getQueryString());
        //返回result 里面有total和结果集
        return new PageResult<>(page.getTotal(), page.getResult());
    }

    /**
     * 删除用户
     *
     * @param id
     */
    @Override
    @Transactional
    public void delete(Integer id) {
        //先删除关系表再删除用户
        userDao.deleteCondition(id);
        userDao.deleteUser(id);
    }

    /**
     * 没有用了这个方法 不回显了摊牌了 太麻烦懒的搞 了
     *
     * @param id
     * @return
     */
    @Override
    public User findById(Integer id) {
        return userDao.findById(id);
    }

    /**
     * 修改用户
     *
     * @param user
     * @param roleIds
     */
    @Override
    @Transactional
    public void update(User user, Integer[] roleIds) {
        //修改用户先要解除原来与角色的关系
        userDao.deleteCondition(user.getId());
        //再修改用户信息
        if (user.getPassword()!=null){
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setPassword(encoder.encode(user.getPassword()));
        }
        userDao.update(user);
        //再建立新关系
        if (null != roleIds || roleIds.length != 0) {
            for (Integer roleId : roleIds) {
                userDao.addRoleRelation(user.getId(), roleId);
            }
        }
    }
}
