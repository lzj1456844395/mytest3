package com.itheima.health.service;

import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;

import java.util.List;

/**
 * Description: No Description
 * User: Eric
 */
public interface CheckGroupService {
    /**
     * 添加检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void add(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 分页条件查询
     * @param queryPageBean
     * @return
     */
    PageResult<CheckGroup> findPage(QueryPageBean queryPageBean);

    /**
     * 通过检查组id查询选中的检查项id
     * @param checkGroupId
     * @return
     */
    List<Integer> findCheckItemIdsByCheckGroupId(int checkGroupId);

    /**
     * 通过id获取检查组
     * @param checkGroupId
     * @return
     */
    CheckGroup findById(int checkGroupId);

    /**
     * 修改检查组
     * @param checkGroup
     * @param checkitemIds
     */
    void update(CheckGroup checkGroup, Integer[] checkitemIds);

    /**
     * 删除检查组
     * @param id
     */
    void deleteById(int id) throws HealthException;

    /**
     * 查询所有检查组
     * @return
     */
    List<CheckGroup> findAll();
}
