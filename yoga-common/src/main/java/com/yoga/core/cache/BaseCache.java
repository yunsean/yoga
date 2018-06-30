package com.yoga.core.cache;


import com.yoga.core.redis.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseCache {
    @Autowired
    protected RedisOperator redis;
}
