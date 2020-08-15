package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.health.dao.MenuDao;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Menu;
import com.itheima.health.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = MenuService.class)
public class MenuServiceImpl implements MenuService {
    @Autowired
    private MenuDao menuDao;

    @Override
    public List<Menu> findMenuByName(String username) {
        //根据用户名得到用户相对应菜单
        List<Menu> list = menuDao.findMenuByName(username);
        List<Menu> resultList = new ArrayList<Menu>();
        // 保存所有菜单id,menu对象, 存储所有父级菜单，将来可以通过菜单的获取菜单对象
        Map<Integer, Menu> menuMap = new HashMap<Integer, Menu>();
        if (null != list) {
            for (Menu menu : list) {
                if (null == menu.getParentMenuId()) {
                    // 一级菜单
                    resultList.add(menu);
                    menuMap.put(menu.getId(), menu);
                }

            }
            // 绑定一二级菜单的关系
            for (Menu menu : list) {
                if (null == menuMap.get(menu.getId())) {
                    //在子集菜单下得到父菜单
                    Menu parentMenu = menuMap.get(menu.getParentMenuId());
                    // 跟在父级菜单下
                    if (null == parentMenu.getChildren()) {
                        parentMenu.setChildren(new ArrayList<Menu>());
                    }
                    parentMenu.getChildren().add(menu);
                }
            }
        }
        return resultList;
    }


    public List<Menu> findoneMenuList() {
        List<Menu> list= menuDao.findAll();
        return list;
    }

    @Override
    public Map<String, Object> findoneMenu(Integer id) {
        Map<String, Object> map = new HashMap<>();
        map.put("menu",menuDao.findoneMenu(id));
        map.put("oneMenuList",menuDao.fuAllMenu());
        return map;

    }

    @Override
    @Transactional
    public void edit(Map<String, Object> map) {
        Menu menu = new Menu();
        //封装
        menu.setId((Integer) map.get("id"));
        menu.setName((String) map.get("name"));
        menu.setLinkUrl((String) map.get("linkUrl"));
        menu.setDescription((String) map.get("description"));
        menu.setIcon((String) map.get("icon"));
        Integer lovel= Integer.valueOf((String)map.get("level"));
        menu.setLevel(lovel);
        menu.setParentMenuId((Integer) map.get("parentMenuId"));
        Integer priority=(Integer) map.get("priority");
        menu.setPriority(priority);

        //判断是否对菜单等级进行修改
        String s = (String) map.get("path");
        if (s.length()>1&&lovel==1){
           throw new HealthException("不能修改菜单等级");
        }else if(s.length()==1&&lovel==2){
            throw new HealthException("不能修改菜单等级");
        }else {
            //没进行菜单等级修改
            menu.setPath(s);
        }
        menuDao.edit(menu);
    }

    @Override
    @Transactional
    public void delete(Integer id) {
        //判断该菜单是否是父菜单
        Menu menu = menuDao.findoneMenu(id);
        if (menu.getPriority()==1){
            //删除此菜单和所有子菜单
            List<Integer> list=menuDao.findParentMenuId(id);//拿到子菜单所有id
            for (Integer x : list) {
                menuDao.deleteById(x);
            }
            menuDao.deleteById(id);
        }else {
            menuDao.deleteById(id);
        }
    }

    @Override
    public List<Menu> getfucaidan() {
        List<Menu> list = menuDao.fuAllMenu();
        return list;
    }

    @Override
    public void add(Menu menu) {
       //判断菜单等级
            if (menu.getLevel()==1) {
            //最大path
            Integer path=menuDao.findMaxPath();
            menu.setPath(path+1+"");
            //最大 pro
            Integer priority=  menuDao.findMaxPriority();
            menu.setPriority(priority+1);
            menu.setParentMenuId(null);
        }else {
            //二级菜单最大path
            String path=menuDao.findMax2Path(menu.getParentMenuId());
            String[] arr=path.split("-");
            Integer integer = Integer.valueOf(arr[1]);
            menu.setPath(arr[0]+"-"+(integer+1));
            //二级最大 pro
            Integer priority=  menuDao.findMax2Priority(menu.getParentMenuId());
            menu.setPriority(priority+1);
        }
        //添加
        menuDao.add(menu);
    }
}
