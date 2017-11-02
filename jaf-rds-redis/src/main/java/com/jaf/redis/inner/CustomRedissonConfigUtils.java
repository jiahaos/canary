package com.jaf.redis.inner;

import org.redisson.config.Config;

import java.util.List;

/**
 * Created by jiahao on 17/3/18.
 */
public class CustomRedissonConfigUtils {

    public static Config getConfig(List<String> addressList) {
        Config config = new Config();
        config.useSingleServer()
                .setConnectTimeout(CustomRedisConfig.DEFAULT_TIMEOUT)
                .setTimeout(3000)
                .setRetryAttempts(3)
                .setRetryInterval(1000)
                .setReconnectionTimeout(3000)
                .setAddress(addressList.get(0));
        return config;
    }

    public static Config getClusterConfig(List<String> addressList) {
        Config config = new Config();
        config.useClusterServers()
                .setScanInterval(2000)
                .setConnectTimeout(CustomRedisConfig.DEFAULT_TIMEOUT)
                .setTimeout(5000)
                .setRetryAttempts(3)
                .setRetryInterval(1000)
                .setReconnectionTimeout(3000)
                .addNodeAddress(addressList.toArray(new String[addressList.size()]));
        return config;
    }
}
