package com.jaf.redis.inner;

import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by jiah on 2017/5/2.
 */
public class RedisPoolUtils {

    private JedisPool pool = null;

    public RedisPoolUtils(String ip, int prot) {
        if (pool == null) {
            pool = new JedisPool(getConfig(), ip, prot, 100000);
        }
    }

    private JedisPoolConfig getConfig() {
        // http://blog.didispace.com/springbootredis/
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(8);
        config.setMaxTotal(-1);
        config.setMinIdle(0);
        config.setMaxWaitMillis(-1);
        config.setTestOnBorrow(false);
        return config;
    }

    /**
     * <p>通过配置对象 ip 端口 构建连接池</p>
     * @param config 配置对象
     * @param ip ip
     * @param prot 端口
     */
    public RedisPoolUtils(JedisPoolConfig config , String ip, int prot){
        if (pool == null) {
            pool = new JedisPool(config,ip,prot,10000);
        }
    }

    /**
     * <p>通过配置对象 ip 端口 超时时间 构建连接池</p>
     * @param config 配置对象
     * @param ip ip
     * @param port 端口
     * @param timeout 超时时间
     */
    public RedisPoolUtils(JedisPoolConfig config , String ip, int port , int timeout){
        if (pool == null) {
            pool = new JedisPool(config,ip,port,timeout);
        }
    }

    /**
     * <p>通过连接池对象 构建一个连接池</p>
     * @param pool 连接池对象
     */
    public RedisPoolUtils(JedisPool pool){
        if (this.pool == null) {
            this.pool = pool;
        }
    }

    public JedisPool getRedisPool() {
        return this.pool;
    }


}
