package com.jaf.redis.inner;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import redis.clients.jedis.HostAndPort;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by hsk on 2017/3/20.
 */
@Slf4j
@Configuration
public class CustomRedisConfiguration {

    @Autowired
    private RedisService redisService;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        JedisConnectionFactory factory;
        if (redisService.isCluster()) {
            Set<HostAndPort> clusterNodes = redisService.getJedisClusterNodes();
            if (clusterNodes == null || clusterNodes.size() == 0) {
                log.error("redis init fail");
                throw new IllegalArgumentException("redis config error");
            }
            Set<String> nodes = clusterNodes.stream().map(p -> p.getHost() + ":" + p.getPort()).collect(Collectors.toSet());
            factory = new JedisConnectionFactory(new RedisClusterConfiguration(nodes));
        } else {
            String[] ipPorts = redisService.getNodes().split(":");
            if (ipPorts.length != 2) {
                log.error("redis init fail . Nodes : " + redisService.getNodes());
                throw new IllegalArgumentException("redis config error. Nodes : " + redisService.getNodes());
            }
            Integer firstPort = Integer.parseInt(ipPorts[1]);
            String firstHost = ipPorts[0];

            factory = new JedisConnectionFactory();
            factory.setPort(firstPort);
            factory.setHostName(firstHost);
        }
        factory.setTimeout(6000);
        factory.setUsePool(true);
        return factory;
    }



}
