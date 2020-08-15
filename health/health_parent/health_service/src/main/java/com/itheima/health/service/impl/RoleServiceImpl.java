package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.StringUtil;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.dao.PermissionDao;
import com.itheima.health.dao.RoleDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Menu;
import com.itheima.health.pojo.Permission;
import com.itheima.health.pojo.Role;
import com.itheima.health.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = RoleService.class)
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDao roleDao;

    @Autowired
    private MenuDao menuDao;

    @Autowired
    private PermissionDao permissionDao;


    @Override
    public Map<String,Object> findMenuAndPermission() {
        Map<String, Object> map = new HashMap<>();
        //查找所用权限
        List<Menu> list1=menuDao.findAll();
        //查找所用菜单
        List<Permission> list2=permissionDao.findAll();
        map.put("menuTableData", list1);
        map.put("permissionTableData", list2);
        return map;
    }

    @Override
    @Transactional
    public void add(Object[] arr) {
        Map<String,String> map = (Map<String, String>) arr[0];
        Role role = new Role();
        role.setName(map.get("name"));
        role.setKeyword(map.get("keyword"));
        role.setDescription(map.get("description"));
        List<Integer> listMId =(List<Integer>) arr[1];
        List<Integer> listPId =(List<Integer>) arr[2];

        //添加角色表并放回主键id
        roleDao.add(role);
        Integer id = role.getId();

        //添加角色菜单表数据
        for (Integer integer : listMId) {
            roleDao.addRoleAndMenu(id,integer);
        }
        //添加角色权限表数据
        for (Integer integer : listPId) {
            roleDao.addRoleAndPermission(id,integer);
        }
    }

    @Override
    public Role findById(Integer id) {
        Role role=roleDao.findById(id);
        return role;
    }

    @Override
    @Transactional
    public void update(Object[] arr) {
        Map<String,Object> map = (Map<String, Object>) arr[0];
        Role role = new Role();
        role.setId((Integer) map.get("id"));
        role.setName((String) map.get("name"));
        role.setKeyword((String)map.get("keyword"));
        role.setDescription((String)map.get("description"));
        List<Integer> listMId =(List<Integer>) arr[1];
        List<Integer> listPId =(List<Integer>) arr[2];

        //修改角色表
        roleDao.update(role);
        //删除关联并添加
        roleDao.deleteRoleAndMenuByRoleId(role.getId());
        roleDao.deleteRoleAndPermissionByRoleId(role.getId());
        //添加角色菜单表数据
        for (Integer integer : listMId) {
            roleDao.addRoleAndMenu(role.getId(),integer);
        }
        //添加角色权限表数据
        for (Integer integer : listPId) {
            roleDao.addRoleAndPermission(role.getId(),integer);
        }
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        //查看与用户是否存在关联
        Integer sum=roleDao.relevance(id);
        if (sum>0) {
            throw new HealthException("存在表关联,不允许删除");
        }
        //删除与菜单的关联
        roleDao.deleteRoleAndMenuByRoleId(id);
        //删除与菜单的关联
        roleDao.deleteRoleAndPermissionByRoleId(id);
        //删除角色
        roleDao.delete(id);
    }

    @Override
    public PageResult<Role> findPage(QueryPageBean queryPageBean) {
        //分页对象
        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        //判断是否需要模糊查询
        if (!StringUtil.isEmpty(queryPageBean.getQueryString())){
            queryPageBean.setQueryString("%"+queryPageBean.getQueryString()+"%");
        }
        //查询
        Page<Role> page=roleDao.findPage(queryPageBean.getQueryString());
        //封装结果
        PageResult<Role> rolePageResult = new PageResult<Role>(page.getTotal(),page.getResult());
        return rolePageResult;
    }
}
