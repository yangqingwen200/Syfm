package com.commons.redis;

import com.alibaba.fastjson.JSONArray;
import com.commons.constant.CommonConstant;
import com.commons.context.AppContext;
import com.commons.service.GenericService;
import net.sf.json.JSONObject;

import java.util.List;
import java.util.Map;

/**
 * Redis缓存中没有, 就去数据库中查询工具类
 * @author Yang
 * @version v1.0
 * @date 2017/9/4
 */
public class RedisUtil {
    private static final GenericService publicService;
    private static final CacheRedisClient cacheRedis;

    static {
        publicService =  AppContext.getBean("publicService", GenericService.class);
        cacheRedis =  AppContext.getBean("cacheRedisClient", CacheRedisClient.class);
    }

    /**
     * 从redis中取出指定key的值, 如果没有, 先从数据库中查出, 再放入redis中<br/><br/>
     * <strong>传参数的顺序: 1.key, 2.sql(可带问号占位符), 3.sql需要参数值(Object[]), 4.缓存时间(Integer)</strong><br/>
     * 如果sql没有?占位符, 第三个参数可以不传, 欲指定缓存时间的话, 第三个参数可以传缓存时间<br/>
     * 如果sql有?占位符, 第三个参数可以必传, 格式: new Object[]{xx, xx}
     * @param param 参数值
     * @return list
     */
    public static List<Object> getListByKey(Object... param) {
        List<Object> list;
        int length = param.length;
        if(param.length == 0) {
            return null;
        }
        Object param0 = param[0];
        String redisKey = CommonConstant.REDIS_UTIL_LIST_PREFIX + String.valueOf(param0);
        String keyValue = cacheRedis.get(redisKey);
        if(null != keyValue) {
            list = JSONArray.parseArray(keyValue);
        } else {
            if(length == 1) {
                return null; //只查询指定的key是否缓存过, 不查询数据库
            }
            Object param1 = param[1];
            if(length > 2) {
                Object param2 = param[2];
                if(param2 instanceof Object[]) { //第三个参数为list, 说明只有key 和 sql 和 sql中?参数值
                    if(!String.valueOf(param1).contains("?")) {
                        throw new RuntimeException("sql语句中没有?占位符, 但不需要传值, 请检查!");
                    }
                    list = publicService.findSqlListMap(String.valueOf(param1), (Object[]) param2);
                    if(length == 3) { //param长度为3, 说明只有key 和 sql 和 sql中?参数值
                        cacheRedis.set(redisKey, JSONArray.toJSONString(list));

                    } else if(length == 4 && param[3] instanceof Integer) { //param长度为4, 说明只有key 和 sql 和 和 sql中?参数值 和缓存时间
                        cacheRedis.set(redisKey, JSONArray.toJSONString(list), (Integer) param[3]);
                    } else {
                        throw new RuntimeException("参数值只能有四个, 并且第四个参数只能是Integer类型, 请检查!");
                    }
                } else if(param2 instanceof Integer) { //第三个参数为Integer, 说明只有key 和 sql 和 和缓存时间
                    if(String.valueOf(param1).contains("?")) {
                        throw new RuntimeException("sql语句中带有?占位符, 但没有传值, 请检查!");
                    }
                    list = publicService.findSqlListMap(String.valueOf(param1));
                    cacheRedis.set(redisKey, JSONArray.toJSONString(list), (Integer) param2);
                } else {
                    throw new RuntimeException("第三个参数有问题, 只能是缓存时间Integer 或者 sql参数值对象数组Object[], 请检查!");
                }
            } else { //param长度为2, 说明只有key 和 sql
                list = publicService.findSqlListMap(String.valueOf(param1));
                cacheRedis.set(redisKey, JSONArray.toJSONString(list));
            }
        }
        return list;
    }

    /**
     * 从redis中取出指定key的值, 如果没有, 先从数据库中查出, 再放入redis中<br/><br/>
     * <strong>传参数的顺序: 1.key, 2.sql(可带问号占位符), 3.sql需要参数值(Object[]), 4.缓存时间(Integer)</strong><br/>
     * 如果sql没有?占位符, 第三个参数可以不传, 欲指定缓存时间的话, 第三个参数可以传缓存时间<br/>
     * 如果sql有?占位符, 第三个参数可以必传, 格式: new Object[]{xx, xx}
     * @param param 参数值
     * @return list
     */
    public static Map<String, Object> getMapByKey(Object... param) {
        Map<String, Object> map;
        int length = param.length;
        if(param.length == 0) {
            return null;
        }
        Object param0 = param[0];
        String redisKey = CommonConstant.REDIS_UTIL_MAP_PREFIX + String.valueOf(param0);
        String keyValue = cacheRedis.get(redisKey);
        if(null != keyValue) {
            map = (Map) JSONObject.fromObject(keyValue);
        } else {
            if(length == 1) {
                return null; //只查询指定的key是否缓存过, 不查询数据库
            }
            Object param1 = param[1];
            if(length > 2) {
                Object param2 = param[2];
                if(param2 instanceof Object[]) { //第三个参数为list, 说明只有key 和 sql 和 sql中?参数值
                    if(!String.valueOf(param1).contains("?")) {
                        throw new RuntimeException("sql语句中没有?占位符, 但不需要传值, 请检查!");
                    }
                    map = publicService.findSqlMap(String.valueOf(param1), (Object[]) param2);
                    if(length == 3) { //param长度为3, 说明只有key 和 sql 和 sql中?参数值
                        cacheRedis.set(redisKey, JSONObject.fromObject(map).toString());

                    } else if(length == 4 && param[3] instanceof Integer) { //param长度为4, 说明只有key 和 sql 和 和 sql中?参数值 和缓存时间
                        cacheRedis.set(redisKey, JSONObject.fromObject(map).toString(), (Integer) param[3]);
                    } else {
                        throw new RuntimeException("参数值只能有四个, 并且第四个参数只能是Integer类型, 请检查!");
                    }
                } else if(param2 instanceof Integer) { //第三个参数为Integer, 说明只有key 和 sql 和 和缓存时间
                    if(String.valueOf(param1).contains("?")) {
                        throw new RuntimeException("sql语句中带有?占位符, 但没有传值, 请检查!");
                    }
                    map = publicService.findSqlMap(String.valueOf(param1));
                    cacheRedis.set(redisKey, JSONObject.fromObject(map).toString(), (Integer) param2);
                } else {
                    throw new RuntimeException("第三个参数有问题, 只能是缓存时间Integer 或者 sql参数值对象数组Object[], 请检查!");
                }
            } else { //param长度为2, 说明只有key 和 sql
                map = publicService.findSqlMap(String.valueOf(param1));
                cacheRedis.set(redisKey, JSONObject.fromObject(map).toString());
            }
        }
        return map;
    }

}
