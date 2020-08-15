package com.itheima.health.service;

import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.Menu;

import java.util.List;
import java.util.Map;

public interface MenuService {
    List<Menu> findMenuByName(String username);

    List<Menu> findoneMenuList();

    Map<String, Object> findoneMenu(Integer id);

    void edit(Map<String, Object> map)throws HealthException;

    void delete(Integer id);

    List<Menu>getfucaidan();


    void add(Menu menu);
}
