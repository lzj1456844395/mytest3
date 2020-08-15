package com.itheima.health.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.health.constant.MessageConstant;
import com.itheima.health.constant.RedisConstant;
import com.itheima.health.entity.PageResult;
import com.itheima.health.entity.QueryPageBean;
import com.itheima.health.entity.Result;
import com.itheima.health.pojo.Setmeal;
import com.itheima.health.service.SetmealService;
import com.itheima.health.utils.QiNiuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Description: No Description
 * User: Eric
 */
@RestController
@RequestMapping("/setmeal")
public class SetmealController {

    @Reference
    private SetmealService setmealService;

    @Autowired
    private JedisPool jedisPool;

    /**
     * 套餐上传图片
     * @param imgFile
     * @return
     */
    @PostMapping("/upload")
    public Result upload(MultipartFile imgFile){
        String originalFilename = imgFile.getOriginalFilename();
        // 获取图片的后缀名 .jpg
        String ext = originalFilename.substring(originalFilename.lastIndexOf("."));
        //- 生成唯一的文件名
        String uniqueName = UUID.randomUUID().toString() + ext;
        //- 上传的图片，调用QiNiuUtils把图片上传到七牛
        Jedis jedis = jedisPool.getResource();
        try {
            QiNiuUtils.uploadViaByte(imgFile.getBytes(), uniqueName);
            jedis.sadd(RedisConstant.SETMEAL_PIC_RESOURCES, uniqueName);
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.PIC_UPLOAD_FAIL);
        } finally {
            if(null != jedis)
            jedis.close(); // 返回jedis连接池
        }
        //- 返回给前端数据,Result(data=Map)
        //{
        //   flag:
        //   message:
        //   data: {
        //      imgName: 图片名称  保存到数据库
        //      domain: 七牛的域名
        //   }
        //}
        Map<String,String> dataMap = new HashMap<String,String>();
        dataMap.put("imgName", uniqueName);
        dataMap.put("domain", QiNiuUtils.DOMAIN);
        return new Result(true, MessageConstant.PIC_UPLOAD_SUCCESS,dataMap);
    }

    @PostMapping("/add")
    public Result add(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        Jedis jedis = jedisPool.getResource();
        // 调用业务服务添加
        setmealService.add(setmeal, checkgroupIds);
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        jedis.close();
        // 响应结果
        return new Result(true, MessageConstant.ADD_SETMEAL_SUCCESS);
    }

    /**
     * 分页查询
     */
    @PostMapping("/findPage")
    public Result findPage(@RequestBody QueryPageBean queryPageBean){
        // 调用服务分页查询
        PageResult<Setmeal> pageResult = setmealService.findPage(queryPageBean);
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,pageResult);
    }

    /**
     * 通过id查询套餐信息
     */
    @GetMapping("/findById")
    public Result findById(int id){
        // 调用服务查询
        Setmeal setmeal = setmealService.findById(id);
        // 前端要显示图片需要全路径
        // setmeal.setImg(QiNiuUtils.DOMAIN + setmeal.getImg());
        // setmeal通过上面的语句，img代表全路径=> formData绑定， img也是全路径 => 提交过来的setmeal.img全路径, 截取字符串 获取图片的名称
        // 封装到map中，解决图片路径问题
        Map<String,Object> resultMap = new HashMap<String,Object>();
        resultMap.put("setmeal", setmeal); // formData
        resultMap.put("imageUrl", QiNiuUtils.DOMAIN + setmeal.getImg()); // imageUrl
        return new Result(true, MessageConstant.QUERY_SETMEAL_SUCCESS,resultMap);
    }

    /**
     * 通过id查询选中的检查组ids
     */
    @GetMapping("/findCheckgroupIdsBySetmealId")
    public Result findCheckgroupIdsBySetmealId(int id){
        List<Integer> checkgroupIds = setmealService.findCheckgroupIdsBySetmealId(id);
        return new Result(true, MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkgroupIds);
    }

    /**
     * 修改
     */
    @PostMapping("/update")
    public Result update(@RequestBody Setmeal setmeal, Integer[] checkgroupIds){
        Jedis jedis = jedisPool.getResource();
        // 旧的套餐数据
        Setmeal oldSetmeal = setmealService.findById(setmeal.getId());
        // 调用业务服务修改
        setmealService.update(setmeal, checkgroupIds);
        // 先删除旧图片
        jedis.srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, oldSetmeal.getImg());
        // 修改时，有可能图片也被改，即没有改图片，set集合，也不会重复
        jedis.sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        jedis.close();
        // 响应结果
        return new Result(true, MessageConstant.EDIT_SETMEAL_SUCCESS);
    }

    /**
     * 删除
     */
    @PostMapping("/deleteById")
    public Result deleteById(int id){
        // 查询要删除的套餐图片名称
        Setmeal setmeal = setmealService.findById(id);
        // 调用服务删除
        setmealService.deleteById(id);
        Jedis jedis = jedisPool.getResource();
        // 删除旧图片
        jedis.srem(RedisConstant.SETMEAL_PIC_DB_RESOURCES, setmeal.getImg());
        return new Result(true, MessageConstant.DELETE_SETMEAL_SUCCESS);
    }
}
