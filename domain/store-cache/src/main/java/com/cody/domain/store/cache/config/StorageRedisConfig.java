package com.cody.domain.store.cache.config;

import com.cody.domain.store.cache.dto.AllUser;
import com.cody.domain.store.cache.dto.DisplayProduct;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.ArrayList;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class StorageRedisConfig {
    private final ObjectMapper objectMapper;
    private final RedisConnectionFactory redisConnectionFactory;
    @Bean
    public RedisTemplate<String, List<DisplayProduct>> redisDisplayProductsTemplate() {
        RedisTemplate<String, List<DisplayProduct>> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(
            new Jackson2JsonRedisSerializer<>(objectMapper, ArrayList.class) {
                @Override
                protected JavaType getJavaType(Class<?> clazz) {
                    if (List.class.isAssignableFrom(clazz)) {
                        return objectMapper.getTypeFactory()
                                           .constructCollectionType(ArrayList.class, DisplayProduct.class);
                    } else {
                        return super.getJavaType(clazz);
                    }
                }
            });
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, DisplayProduct> redisDisplayProductTemplate() {
        Jackson2JsonRedisSerializer<DisplayProduct> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, DisplayProduct.class);
        RedisTemplate<String, DisplayProduct> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    public RedisTemplate<String, AllUser> redisAllUserTemplate() {
        Jackson2JsonRedisSerializer<AllUser> jackson2JsonRedisSerializer = new Jackson2JsonRedisSerializer<>(objectMapper, AllUser.class);
        RedisTemplate<String, AllUser> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(jackson2JsonRedisSerializer);
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        return redisTemplate;
    }
}
