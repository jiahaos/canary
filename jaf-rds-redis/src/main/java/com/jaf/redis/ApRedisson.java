package com.jaf.redis;


import com.jaf.redis.inner.CustomRedissonConfigUtils;
import com.jaf.redis.inner.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.stereotype.Service;
import redis.clients.jedis.HostAndPort;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * Created by hsk on 16/6/11.
 */
@Slf4j
@Service
public class ApRedisson {

    @Value("${spring.profiles.active}")
    private String springProfilesActive;

    private RedissonClient redisson;

    @Autowired
    private RedisService redisService;

    @PostConstruct
    private void init() {
        Set<HostAndPort> hosts = redisService.getJedisClusterNodes();
        if (hosts == null || hosts.size() == 0) {
            log.error("redis集群配置获取失败,redisson初始化失败");
            return;
        }
        List<String> addressList = hosts.stream()
                .filter(item -> item != null)
                .map(item -> item.getHost() + ":" + item.getPort())
                .collect(Collectors.toList());

        Config config;
        if (redisService.isCluster())
            config = CustomRedissonConfigUtils.getClusterConfig(addressList);
        else config = CustomRedissonConfigUtils.getConfig(addressList);

        this.redisson = Redisson.create(config);
    }

    public RedissonClient getRedisson() {
        return redisson;
    }

    public boolean lock(String key, int lockTimeout) throws InterruptedException {
        RLock lock = redisson.getLock(springProfilesActive + ":" + key);
        if (lock.isLocked()) {
            return false;
        }
        return lock.tryLock(lockTimeout, TimeUnit.SECONDS);
    }

    public void unlock(String key) {
        redisson.getLock(springProfilesActive + ":" + key).unlock();
    }
}
