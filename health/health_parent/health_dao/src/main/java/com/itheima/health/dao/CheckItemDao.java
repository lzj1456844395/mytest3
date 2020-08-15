package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckItem;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckItemDao {
    /**
     * 查询 所有检查项
     * @return
     */
    List<CheckItem> findAll();

    /**
     * 新增检查项
     * @param checkitem
     */
    void add(CheckItem checkitem);

    /**
     * 分页条件查询
     * @return
     */
    Page<CheckItem> findByCondition(String queryString);

    /**
     * 检查 检查项是否被检查组使用了
     * @param id
     * @return
     */
    int findCountByCheckItemId(int id);

    /**
     * 通过id删除检查项
     * @param id
     */
    void deleteById(int id);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    CheckItem findById(int id);

    /**
     * 更新检查项
     * @param checkitem
     */
    void update(CheckItem checkitem);

    List<CheckItem> findByCondition2(String queryString);
}
