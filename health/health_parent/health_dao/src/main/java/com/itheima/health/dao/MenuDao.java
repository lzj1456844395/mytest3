package com.itheima.health.dao;

import com.itheima.health.pojo.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public interface MenuDao {
    List<Menu> findMenuByName(String username);

    List<Menu> findAll();

    Menu findoneMenu(Integer id);

    List<Menu> fuAllMenu();

    void edit(Menu menu);


    void deleteById(Integer id);

    List<Integer> findParentMenuId(Integer id);

    Integer findMaxPath();

    Integer findMaxPriority();

    String findMax2Path(Integer parentMenuId);

    Integer findMax2Priority(Integer parentMenuId);

    void add(Menu menu);
}





