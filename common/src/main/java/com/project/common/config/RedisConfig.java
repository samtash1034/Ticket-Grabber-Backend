package com.project.common.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.util.Arrays;

@Configuration
public class RedisConfig {

    @Primary
    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        RedisClusterConfiguration clusterConfig = new RedisClusterConfiguration(
                // TODO 換成自己的 ip (這邊無法使用 localhost)
                Arrays.asList("192.168.0.109:7001", "192.168.0.109:7002", "192.168.0.109:7003"));
        clusterConfig.setMaxRedirects(3);
        return new LettuceConnectionFactory(clusterConfig);
    }

    @Primary
    @Bean(name = "redisTemplate")
    public RedisTemplate<String, String> redisTemplate() {
        RedisTemplate<String, String> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory()); // 使用集群的連接工廠

        // 設置鍵和值的序列化器為 StringRedisSerializer
        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(stringSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(stringSerializer);

        return template;
    }

    @Bean(name = "objectRedisTemplate")
    public RedisTemplate<String, Object> objectRedisTemplate() {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory());

        StringRedisSerializer stringSerializer = new StringRedisSerializer();
        GenericJackson2JsonRedisSerializer jsonSerializer = new GenericJackson2JsonRedisSerializer();

        template.setKeySerializer(stringSerializer);
        template.setValueSerializer(jsonSerializer);
        template.setHashKeySerializer(stringSerializer);
        template.setHashValueSerializer(jsonSerializer);

        return template;
    }
}
