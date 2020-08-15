package com.itheima.health.dao;

import com.github.pagehelper.Page;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
public interface SetmealDao {
    /**
     * 添加套餐
     * @param setmeal
     */
    void add(Setmeal setmeal);

    /**
     * 添加套餐与检查组的关系
     * @param setmealId
     * @param checkgroupId
     */
    void addSetmealCheckGroup(@Param("setmealId") Integer setmealId, @Param("checkgroupId") Integer checkgroupId);

    /**
     * 条件查询
     * @param queryString
     * @return
     */
    Page<Setmeal> findByCondition(String queryString);

    /**
     * 通过id查询
     * @param id
     * @return
     */
    Setmeal findById(int id);

    /**
     * 通过id查询选中的检查组ids
     * @param id
     * @return
     */
    List<Integer> findCheckgroupIdsBySetmealId(int id);

    /**
     * 更新套餐信息
     * @param setmeal
     */
    void update(Setmeal setmeal);

    /**
     * 删除旧关系
     * @param id
     */
    void deleteSetmealCheckGroup(Integer id);

    /**
     * 统计使用了这个套餐的订单个数
     * @param id
     * @return
     */
    int findOrderCountBySetmealId(int id);

    /**
     * 通过id删除套餐
     * @param id
     */
    void deleteById(int id);

    /**
     * 查询所有的套餐
     * @return
     */
    List<Setmeal> findAll();

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    Setmeal findDetailById(int id);
    /**
     * 查询套餐详情 方式二
     * @param id
     * @return
     */
    Setmeal findDetailById2(int id);


    /**
     * 通过套餐id查询检查组列表
     * @param id
     * @return
     */
    List<CheckGroup> findCheckGroupListBySetmealId(Integer id);

    List<CheckGroup> findCheckGroupListBySetmealId02(Integer id);
    /**
     * 通过检查组id检查检查项列表
     * @param id
     * @return
     */
    List<CheckItem> findCheckItemByCheckGroupId(Integer id);

    List<CheckItem> findCheckItemByCheckGroupId02(Integer id);
    /**
     * 获取套餐的预约数量
     * @return
     */
    List<Map<String, Object>> findSetmealCount();
}
