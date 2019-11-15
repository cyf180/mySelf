package com.tdpro.common.utils;

import org.springframework.data.geo.*;
import org.springframework.data.redis.connection.RedisGeoCommands;
import org.springframework.data.redis.core.GeoOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.util.concurrent.TimeUnit;

/**
 * @About 地理位置信息缓存工具类
 * @Author jy
 * @Date 2018/6/7 16:53
 */
public class RedisGeoUtil<T> {
    private RedisTemplate<String, Object> redisTemplate;

    public RedisGeoUtil(RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public void setRedisTemplate(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 缓存地理位置信息
     *
     * @param key
     * @param x
     * @param y
     * @param value
     * @param time
     */
    public void geoAdd(String key, double x, double y, T value, long time) {
        GeoOperations<String, T> geoOps = (GeoOperations<String, T>) redisTemplate.opsForGeo();
        geoOps.geoAdd(key, new Point(x, y), value);
        if (time > 0) {
            redisTemplate.expire(key, time, TimeUnit.SECONDS);
        }
    }

    /**
     * 通过给定的坐标和距离(km)获取范围内其它的坐标信息
     */
    public GeoResults<RedisGeoCommands.GeoLocation<T>> radiusGeo(String key, double x, double y, double range) {
        GeoOperations<String, T> geoOps = (GeoOperations<String, T>) redisTemplate.opsForGeo();
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        args.includeCoordinates().includeDistance().sortAscending();
        return geoOps.geoRadius(key, new Circle(new Point(x, y), new Distance(range, Metrics.KILOMETERS)), args);
    }

    /**
     * 通过给定的坐标和距离(km)获取范围内其它的坐标信息
     */
    public GeoResults<RedisGeoCommands.GeoLocation<T>> radiusGeoLimit(String key, double x, double y, double range, int count) {
        GeoOperations<String, T> geoOps = (GeoOperations<String, T>) redisTemplate.opsForGeo();
        RedisGeoCommands.GeoRadiusCommandArgs args = RedisGeoCommands.GeoRadiusCommandArgs.newGeoRadiusArgs();
        args.includeCoordinates().includeDistance().sortDescending().limit(count);
        return geoOps.geoRadius(key, new Circle(new Point(x, y), new Distance(range, Metrics.KILOMETERS)), args);
    }


}
