package com.yoga.core.redis.lock;

import com.yoga.core.utils.StrUtil;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.config.Config;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

@Component
public class RedissonConnector {

    @Value("${spring.redis.host:localhost}")
    private String redisHost;
    @Value("${spring.redis.port:6379}")
    private int redisPort;
    @Value("${spring.redis.database:0}")
    private int redisDatabase;
    @Value("${spring.redis.password:}")
    private String redisPassword;

    private RedissonClient redissonClient;

    @PostConstruct
    public void init(){
        //https://github.com/redisson/redisson/wiki/2.-%E9%85%8D%E7%BD%AE%E6%96%B9%E6%B3%95
        Config config = new Config();
        config.useSingleServer()
                .setAddress(redisHost + ":" + redisPort)
                .setDatabase(redisDatabase);
        if (StrUtil.isNotBlank(redisPassword)) {
            config.useSingleServer().setPassword(redisPassword);
        }
        redissonClient = Redisson.create(config);
    }

    public RedissonClient getClient(){
        return redissonClient;
    }

}