package com.yoga.core.base;

import com.yoga.core.redis.RedisOperator;
import org.springframework.beans.factory.annotation.Autowired;

public class BaseCache {
    @Autowired
    protected RedisOperator redisOperator;
}
