package com.jaf.redis.inner;

import com.google.common.collect.Sets;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import redis.clients.jedis.*;

import javax.annotation.PostConstruct;
import java.util.Set;


/**
 * Created by jiahao on 17/3/17.
 */
@Slf4j
@Configuration
public class RedisService {

    @Autowired
    private Environment environment;
    private JedisPool pool;

    private String nodes;
    private String maxRedirects;
    private String requirepass;

    private Set<HostAndPort> jedisClusterNodes;
    private JedisCluster jedisCluster;
    private RedisEnum redisType;

    @PostConstruct
    public void init() {
        this.nodes = environment.getProperty("spring.redis.cluster.nodes");
        this.maxRedirects = environment.getProperty("spring.redis.cluster.max-redirects");
        this.requirepass = environment.getProperty("spring.redis.cluster.requirepass");
        if (nodes == null || "".equals(nodes)) {
            log.info("未找到redis的配置");
            return;
        }
        try {
            this.jedisClusterNodes = decodeArguments(nodes);
            if(jedisClusterNodes.size() == 1) {
                redisType = RedisEnum.SIMPLE;
                pool = createJedisPool(jedisClusterNodes, requirepass);
            }else {
                jedisCluster = createJedisCluster(jedisClusterNodes, maxRedirects);
                redisType = RedisEnum.CLUSTER;
            }
        } catch (Exception e) {
            log.error("解析redis的配置错误", e);
        }
    }

    public static Set<HostAndPort> decodeArguments(String nodes) {
        Set<HostAndPort> rs = Sets.newHashSet();
        String[] nodeItems = nodes.split(",");
        for (String nodeItem : nodeItems) {
            String[] ipPorts = nodeItem.split(":");
            rs.add(new HostAndPort(ipPorts[0], Integer.valueOf(ipPorts[1])));
        }
        return rs;
    }

    public JedisPool createJedisPool(Set<HostAndPort> jedisClusterNodes, String requirepass) {
        HostAndPort jedisClusterNode = (HostAndPort) jedisClusterNodes.toArray()[0];
        RedisPoolUtils poolUtils = new RedisPoolUtils(jedisClusterNode.getHost(), jedisClusterNode.getPort(), requirepass);
        return poolUtils.getRedisPool();
    }

    public void closeJedis(Jedis jedis) {
        if(jedis != null)
            jedis.close();
    }

    public JedisCluster createJedisCluster(Set<HostAndPort> jedisClusterNodes, String maxRedirects) {
        if (jedisClusterNodes == null || jedisClusterNodes.size() == 0) {
            return null;
        }
        if (maxRedirects == null || "".equals(maxRedirects)) {
            jedisCluster = new JedisCluster(jedisClusterNodes, CustomRedisConfig.DEFAULT_TIMEOUT);
        } else {
            jedisCluster = new JedisCluster(jedisClusterNodes, CustomRedisConfig.DEFAULT_TIMEOUT, Integer.valueOf(maxRedirects));
        }
        return jedisCluster;
    }

    public Set<HostAndPort> getJedisClusterNodes() {
        return jedisClusterNodes;
    }

    public JedisCluster getJedisCluster() {
        return jedisCluster;
    }

    public JedisPool getJedisPool() {
        return pool;
    }

    public boolean isCluster() {
        return redisType.equals(RedisEnum.CLUSTER);
    }

    public String getNodes() {
        return nodes;
    }

}
