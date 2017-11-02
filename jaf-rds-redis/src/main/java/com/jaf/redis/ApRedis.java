package com.jaf.redis;


import com.google.common.collect.Sets;
import com.jaf.redis.inner.RedisService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.exceptions.JedisMovedDataException;

import java.util.*;

/**
 * Created by hsk on 16/5/25.
 */
@Service
public class ApRedis {

    private static Logger logger = LoggerFactory.getLogger(ApRedis.class);

    @Autowired
    private RedisService redisService;

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    public String hget(String key, String field) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hget(springProfilesActive + ":" + key, field);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hget(springProfilesActive + ":" + key, field);
        }finally {
            redisService.closeJedis(jedis);
        }

    }

    public Long hset(final String key, final String field, final String value) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hset(springProfilesActive + ":" + key, field, value);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hset(springProfilesActive + ":" + key, field, value);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public String hmset(final String key, final Map<String, String> hash) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hmset(springProfilesActive + ":" + key, hash);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hmset(springProfilesActive + ":" + key, hash);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public List<String> hmget(final String key, final String... fields) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hmget(springProfilesActive + ":" + key, fields);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hmget(springProfilesActive + ":" + key, fields);
        }finally {
            redisService.closeJedis(jedis);
        }

    }

    public List<String> hmget(final String key, final Set<String> fields) {
        if (fields == null || fields.size() == 0) {
            return null;
        }
        if (redisService.isCluster())
            return redisService.getJedisCluster().hmget(springProfilesActive + ":" + key, fields.toArray(new String[fields.size()]));

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hmget(springProfilesActive + ":" + key, fields.toArray(new String[fields.size()]));
        }finally {
            redisService.closeJedis(jedis);
        }

    }

    public Boolean hexists(final String key, final String field) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hexists(springProfilesActive + ":" + key, field);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hexists(springProfilesActive + ":" + key, field);
        }finally {
            redisService.closeJedis(jedis);
        }

    }

    public Set<String> hkeys(final String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hkeys(springProfilesActive + ":" + key);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hkeys(springProfilesActive + ":" + key);
        }finally {
            redisService.closeJedis(jedis);
        }

    }

    public List<String> hvals(final String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hvals(springProfilesActive + ":" + key);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hvals(springProfilesActive + ":" + key);
        }finally {
            redisService.closeJedis(jedis);
        }


    }

    public Long hdel(final String key, final String... field) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().hdel(springProfilesActive + ":" + key, field);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hdel(springProfilesActive + ":" + key, field);
        }finally {
            redisService.closeJedis(jedis);
        }

    }

    public Long hdel(final String key, final Set<String> fields) {
        if (fields == null || fields.size() == 0) {
            return null;
        }
        if (redisService.isCluster())
            return redisService.getJedisCluster().hdel(springProfilesActive + ":" + key, fields.toArray(new String[fields.size()]));

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.hdel(springProfilesActive + ":" + key, fields.toArray(new String[fields.size()]));
        }finally {
            redisService.closeJedis(jedis);
        }

    }

    // set

    public Long sadd(final String key, String... value) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().sadd(springProfilesActive + ":" + key, value);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.sadd(springProfilesActive + ":" + key, value);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long sadd(final String key, Collection<String> value) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().sadd(springProfilesActive + ":" + key, value.toArray(new String[value.size()]));

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.sadd(springProfilesActive + ":" + key, value.toArray(new String[value.size()]));
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long srem(final String key, String value) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().srem(springProfilesActive + ":" + key, value);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.srem(springProfilesActive + ":" + key, value);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long srems(final String key, String... values) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().srem(springProfilesActive + ":" + key, values);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.srem(springProfilesActive + ":" + key, values);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Set<String> smembers(String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().smembers(springProfilesActive + ":" + key);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.smembers(springProfilesActive + ":" + key);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    // cluster migrant

    public String set(final String key, String value) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().set(springProfilesActive + ":" + key, value);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.set(springProfilesActive + ":" + key, value);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public String setex(final String key, int seconds, String value) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().setex(springProfilesActive + ":" + key, seconds, value);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.setex(springProfilesActive + ":" + key, seconds, value);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public String get(final String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().get(springProfilesActive + ":" + key);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.get(springProfilesActive + ":" + key);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public boolean exists(final String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().exists(springProfilesActive + ":" + key);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.exists(springProfilesActive + ":" + key);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    /**
     * 获取指定前缀的缓存Keys
     */
    public Set<String> keys(String pattern) {
        Set<String> keys = Sets.newHashSet();
        Map<String, JedisPool> clusterNodes = redisService.getJedisCluster().getClusterNodes();
        for (String k : clusterNodes.keySet()) {
            logger.debug("Getting keys from: {}", k);
            JedisPool jp = clusterNodes.get(k);
            Jedis connection = null;
            try {
                connection = jp.getResource();
                keys.addAll(connection.keys(springProfilesActive + ":" + pattern));
            } catch (Exception e) {
                logger.error("Getting keys error: {}", e);
            } finally {
                logger.debug("Connection closed.");
                if (connection != null) {
                    connection.close();//用完一定要close这个链接！！！
                }
            }
        }
        logger.debug("Keys gotten!");
        return keys;
    }

    /**
     * 批量删除指定前缀的keys
     *
     * @param pattern
     * @return
     */
    public Long batchDel(String pattern) {
        if (redisService.isCluster()) {
            Map<String, JedisPool> clusterNodes = redisService.getJedisCluster().getClusterNodes();
            for (String k : clusterNodes.keySet()) {
                JedisPool jp = clusterNodes.get(k);
                Jedis connection = jp.getResource();
                del(connection, pattern);
            }
        } else {

            Jedis connection = redisService.getJedisPool().getResource();
            del(connection, pattern);
        }
        return 1L;
    }

    private void del(Jedis connection, String pattern) {
        try {
            Set<String> set = connection.keys(springProfilesActive + ":" + pattern);
            Iterator<String> it = set.iterator();
            while (it.hasNext()) {
                String key = it.next();
                connection.del(key);
            }
        } catch (JedisMovedDataException e) {
            logger.error("moved error: {}", e.getMessage());
        } catch (Exception e) {
            logger.error("Getting keys error: {}", e);
        } finally {
            logger.debug("Connection closed.");
            if (connection != null) {
                connection.close();//用完一定要close这个链接！！！
            }
        }
    }

    public Long expire(final String key, final int seconds) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().expire(springProfilesActive + ":" + key, seconds);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.expire(springProfilesActive + ":" + key, seconds);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long del(final String... keys) {
        String[] convertKeys = new String[]{};
        for (int i = 0; i < keys.length; i++) {
            convertKeys[i] = springProfilesActive + ":" + keys[i];
        }
        if (redisService.isCluster())
            return redisService.getJedisCluster().del(convertKeys);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.del(convertKeys);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long del(String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().del(springProfilesActive + ":" + key);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.del(springProfilesActive + ":" + key);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long incr(String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().incr(springProfilesActive + ":" + key);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.incr(springProfilesActive + ":" + key);
        }finally {
            redisService.closeJedis(jedis);
        }
    }
    public Long incrBy(String key, long integer) {

        if (redisService.isCluster())
            return redisService.getJedisCluster().incrBy(springProfilesActive + ":" + key, integer);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.incrBy(springProfilesActive + ":" + key, integer);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long lpush(String key, String... values) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().lpush(springProfilesActive + ":" + key, values);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.lpush(springProfilesActive + ":" + key, values);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public Long rpush(String key, String... values) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().rpush(springProfilesActive + ":" + key, values);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.rpush(springProfilesActive + ":" + key, values);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public List<String> mget(List<String> keys) {
        String[] arryKeys = new String[keys.size()];
        for (int i = 0; i < keys.size(); i++) {
            arryKeys[i] = springProfilesActive + ":" + keys.get(i);
        }
        if (redisService.isCluster())
            return redisService.getJedisCluster().mget(arryKeys);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.mget(arryKeys);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    public String mset(Map values) {
        if (values.isEmpty()) {
            return null;
        }
        String[] keysValues = new String[values.size() * 2];
        int i = 0;
        for (Object object : values.entrySet()) {
            Map.Entry entry = (Map.Entry) object;
            keysValues[i] = springProfilesActive + ":" + entry.getKey();
            i++;
            keysValues[i] = entry.getValue().toString();
            i++;
        }
        if (redisService.isCluster())
            return redisService.getJedisCluster().mset(keysValues);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.mset(keysValues);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    /**
     * 取某Key所有元素
     *
     * @param key
     * @return
     */
    public List<String> lrange(String key) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().lrange(springProfilesActive + ":" + key, 0, -1);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.lrange(springProfilesActive + ":" + key, 0, -1);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    /**
     * 取某Key元素子集
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public List<String> lrange(String key, int start, int end) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().lrange(springProfilesActive + ":" + key, start, end);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.lrange(springProfilesActive + ":" + key, start, end);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

    /**
     * 删除list特定值元素
     *
     * @param key
     * @return
     */
    public long lrem(String key, int count, String value) {
        if (redisService.isCluster())
            return redisService.getJedisCluster().lrem(springProfilesActive + ":" + key, count, value);

        Jedis jedis = redisService.getJedisPool().getResource();
        try {
            return jedis.lrem(springProfilesActive + ":" + key, count, value);
        }finally {
            redisService.closeJedis(jedis);
        }
    }

}
