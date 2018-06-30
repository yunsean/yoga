package com.yoga.core.redis;
import java.lang.reflect.Method;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachingConfigurerSupport;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectMapper.DefaultTyping;

@Configuration  
@EnableCaching  
public class RedisConfiguration extends CachingConfigurerSupport{
  
    @Bean  
    public KeyGenerator wiselyKeyGenerator(){  
        return new KeyGenerator() {  
            @Override  
            public Object generate(Object target, Method method, Object... params) {  
                StringBuilder sb = new StringBuilder();  
                sb.append(target.getClass().getName());
                sb.append(".");
                sb.append(method.getName());
                for (Object obj : params) {
                    sb.append(".");
                    if (obj == null) sb.append("(null)");
                    else sb.append(obj.toString());
                }  
                return sb.toString();  
            }  
        };  
  
    }  
    @Bean  
    public CacheManager cacheManager(RedisTemplate<?, ?> redisTemplate) {  
        RedisCacheManager manage = new RedisCacheManager(redisTemplate);
        manage.setDefaultExpiration(300);
    	return manage;
    }  
  
    @Bean  
    public RedisTemplate<String, String> redisTemplate(RedisConnectionFactory factory) {  
        StringRedisTemplate template = new StringRedisTemplate(factory); 
        Jackson2JsonRedisSerializer<Object> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<Object>(Object.class);  
        ObjectMapper om = new ObjectMapper();  
        om.setVisibility(PropertyAccessor.ALL, Visibility.ANY);  
        om.enableDefaultTyping(DefaultTyping.NON_FINAL);  
        jackson2JsonRedisSerializer.setObjectMapper(om);  
        template.setValueSerializer(jackson2JsonRedisSerializer);  
        template.afterPropertiesSet();  
        return template;  
    }  
}  
