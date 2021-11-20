package com.yoga.core.redis;

import jodd.util.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

@Slf4j
@Configuration
@EnableCaching
public class JedisConfiguration {

    @Value("${spring.redis.host:localhost}")
    private String host;
    @Value("${spring.redis.port:6379}")
    private int port;
    @Value("${spring.redis.password:}")
    private String password;
    @Value("${spring.redis.timeout:0}")
    private int timeout;
    @Value("${spring.redis.pool.max-idle:8}")
    private int maxIdle;
    @Value("${spring.redis.pool.max-wait:-1}")
    private long maxWaitMillis;
    @Value("${spring.redis.database:0}")
    private int database;

    @Bean
    public JedisPool redisPoolFactory() {
        JedisPoolConfig jedisPoolConfig = new JedisPoolConfig();
        jedisPoolConfig.setMaxIdle(maxIdle);
        jedisPoolConfig.setMaxWaitMillis(maxWaitMillis);
        if (StringUtil.isBlank(password)) password = null;
        return new JedisPool(jedisPoolConfig, host, port, timeout, password, database);
    }
}
