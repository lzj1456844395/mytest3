package com.itheima.health.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.dao.SetmealDao;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.exception.HealthException;
import com.itheima.health.pojo.CheckGroup;
import com.itheima.health.pojo.CheckItem;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.*;
import java.util.List;
import java.util.Map;

/**
 * Description: No Description
 * User: Eric
 */
@Service(interfaceClass = SetmealService.class)
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private JedisPool jedisPool;

    @Autowired
    private SetmealDao setmealDao;

    @Autowired
    private FreeMarkerConfigurer freeMarkerConfigurer;

    /**
     * 存放生成静态化页面的目录，开发时，存到health_mobile/webapp/pages下，
     * 上线时，要存放到health_mobile的tomcat目录下
     */
    @Value("${out_put_path}")
    private String out_put_path;

    /**
     * 添加套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void add(Setmeal setmeal, Integer[] checkgroupIds) {
        // 添加套餐信息
        setmealDao.add(setmeal);
        // 获取套餐的id
        Integer setmealId = setmeal.getId();
        // 添加套餐与检查组的关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmealId, checkgroupId);
            }
        }
        //新增套餐后需要重新生成静态页面
        //generateMobileStaticHtml();

        // 获取redis连接
        Jedis jedis = jedisPool.getResource();
        jedis.del(""+setmeal.getId());
    }

    /**
     * 生成 列表及详情静态页面
     */
    /*private void generateMobileStaticHtml(){
        try {
            // 套餐列表静态页面
            generateSetmealListHtml();
            // 套餐详情静态页面 生成单就行了，为了测试方便，生成所有的
            generateSetmealDetailHtml();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }*/

    /**
     * 生成套餐详情静态页面
     */
   /* private void generateSetmealDetailHtml() throws Exception {
        // 把所有套餐都生成详情页面 方便测试
        List<Setmeal> setmealList = setmealDao.findAll();
        // setmealList中的套餐是没有详情信息，即没有检查组也没有检查项的信息，要查询一遍
        for (Setmeal setmeal : setmealList) {
            // 获取套餐详情
            Setmeal setmealDetail = setmealDao.findDetailById(setmeal.getId());
            // 设置套餐的图片路径
            setmealDetail.setImg(QiNiuUtils.DOMAIN + setmealDetail.getImg());
            // 生成详情页面
            generateDetailHtml(setmealDetail);
        }
    }*/

    /**
     * 生成套餐详情页面
     * @param setmealDetail
     */
    /*private void generateDetailHtml(Setmeal setmealDetail) throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal_detail.ftl");
        Map<String, Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmeal", setmealDetail);
        File setmealDetailFile = new File(out_put_path, "setmeal_" + setmealDetail.getId() + ".html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealDetailFile),"utf-8")));
    }*/

    /**
     * 生成 套餐列表静态页面
     */
    /*private void generateSetmealListHtml() throws Exception {
        // 获取模板 套餐列表的模板
        Template template = freeMarkerConfigurer.getConfiguration().getTemplate("mobile_setmeal.ftl");
        // 获取数据模型
        List<Setmeal> setmealList = setmealDao.findAll();
        // 图片地址
        setmealList.forEach(s->{
            s.setImg(QiNiuUtils.DOMAIN + s.getImg());
        });
        Map<String, Object> dataMap = new HashMap<String,Object>();
        dataMap.put("setmealList", setmealList);
        // 给模板填充数据 new OutputStreamWriter要指定编码格式，否则中文乱码
        // 生成的文件 c:/sz89/health_parent/health_mobile/src/main/webapp/pages/m_setmeal.html
        File setmealListFile = new File(out_put_path, "m_setmeal.html");
        template.process(dataMap, new BufferedWriter(new OutputStreamWriter(new FileOutputStream(setmealListFile),"utf-8")));
    }*/

    /**
     * 分页查询
     * @param queryPageBean
     * @return
     */
    @Override
    public PageResult<Setmeal> findPage(QueryPageBean queryPageBean) {

        PageHelper.startPage(queryPageBean.getCurrentPage(), queryPageBean.getPageSize());
        // 查询条件
        if(!StringUtils.isEmpty(queryPageBean.getQueryString())){
            // 模糊查询 %
            queryPageBean.setQueryString("%" + queryPageBean.getQueryString() + "%");
        }
        // 条件查询，这个查询语句会被分页
        Page<Setmeal> page = setmealDao.findByCondition(queryPageBean.getQueryString());
        return new PageResult<Setmeal>(page.getTotal(), page.getResult());
    }

    @Override
    public Setmeal findById(int id) {
        return setmealDao.findById(id);
    }

    /**
     * 通过id查询选中的检查组ids
     * @param id
     * @return
     */
    @Override
    public List<Integer> findCheckgroupIdsBySetmealId(int id) {
        return setmealDao.findCheckgroupIdsBySetmealId(id);
    }

    /**
     * 修改套餐
     * @param setmeal
     * @param checkgroupIds
     */
    @Override
    @Transactional
    public void update(Setmeal setmeal, Integer[] checkgroupIds) {

        // 先更新套餐信息
        setmealDao.update(setmeal);
        // 删除旧关系
        setmealDao.deleteSetmealCheckGroup(setmeal.getId());
        // 添加新关系
        if(null != checkgroupIds){
            for (Integer checkgroupId : checkgroupIds) {
                setmealDao.addSetmealCheckGroup(setmeal.getId(), checkgroupId);
            }
        }

        // 获取redis连接
        Jedis jedis = jedisPool.getResource();
        jedis.del(""+setmeal.getId());
    }

    /**
     * 通过id删除套餐
     * @param id
     */
    @Transactional
    @Override
    public void deleteById(int id) {
        // 统计使用了这个套餐的订单个数，判断套餐是否被使用了
        int count = setmealDao.findOrderCountBySetmealId(id);
        if(count > 0){
            // 被使用了
            throw new HealthException(MessageConstant.SETMEAL_IN_USE);
        }
        // 没有使用
        // 先删除套餐与检查组的关系
        setmealDao.deleteSetmealCheckGroup(id);
        // 删除套餐数据
        setmealDao.deleteById(id);

        // 删除redis对应的key
        // 获取redis连接
        Jedis jedis = jedisPool.getResource();
        jedis.del(""+id);
    }

    /**
     * 查询所有的套餐
     * @return
     */
    @Override
    @Transactional
    public List<Setmeal> findAll() {
        // 获取redis连接
       Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();

        List<Setmeal> setmealList = null;

        try {
        // 先判断redis缓存数据库中套餐列表数据是否存在
        if (jedis.exists("SetMealList")){

            // 获取redis中指定的key
            String json = jedis.get("SetMealList");

            // 将json字符串转换为java对象
            setmealList = mapper.readValue(json, new TypeReference<List<Setmeal>>(){});

        }else {
            //不存在第一次则查询数据库 存入Redis中
            setmealList = setmealDao.findAll();




            // 转换为json字符串
            String json = mapper.writeValueAsString(setmealList);

            // 同步到redis中
            jedis.set("SetMealList", json);
        }

        } catch (IOException e) {
            e.printStackTrace();
        }

        // 连接归还jedis连接
        jedis.close();

        // 返回套餐数据
        return setmealList;


    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById(int id) {
        return setmealDao.findDetailById(id);
    }

    /**
     * 查询套餐详情
     * @param id
     * @return
     */
    @Override
    public Setmeal findDetailById2(int id) {
        return setmealDao.findDetailById2(id);
    }


    @Override
    public Setmeal findDetailById3(int id) {

        Setmeal setmeal = null;
        // 获取redis连接
        Jedis jedis = jedisPool.getResource();
        ObjectMapper mapper = new ObjectMapper();


        try {
            // 先查询redis是否有套餐详情的数据
            if (jedis.exists("setmeal_detail_"+id)){

                // 获取指定的key
                String json = jedis.get("setmeal_detail_"+id);

                // 转换为java对象
                setmeal = mapper.readValue(json,Setmeal.class);
            }else {
                // 查询套餐信息
                setmeal = setmealDao.findById(id);

                // 缓存穿透解决
                if (setmeal == null){
                    // 存入null值 key设置为传入的id  设置key的有效时间为60秒
                    jedis.setex("id",60,null);

                    // 给前端返回null值
                    setmeal = null;
                }else {
                    // 查询套餐下的检查组
                    List<CheckGroup> checkGroups = setmealDao.findCheckGroupListBySetmealId02(id);
                    if (null != checkGroups) {
                        for (CheckGroup checkGroup : checkGroups) {
                            // 通过检查组id检查检查项列表
                            List<CheckItem> checkItems = setmealDao.findCheckItemByCheckGroupId02(checkGroup.getId());
                            // 设置这个检查组下所拥有的检查项
                            checkGroup.setCheckItems(checkItems);
                        }
                        //设置套餐下的所拥有的检查组
                        setmeal.setCheckGroups(checkGroups);
                    }

                    // 转为json格式
                    String json = mapper.writeValueAsString(setmeal);

                    // 第一次查询数据库 查询好后存入redis中
                    jedis.set("setmeal_detail_"+id, json);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }

        // 归还jedis连接
        jedis.close();
        return setmeal;
    }

    /**
     * 获取套餐的预约数量
     * @return
     */
    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetmealCount();
    }
}
