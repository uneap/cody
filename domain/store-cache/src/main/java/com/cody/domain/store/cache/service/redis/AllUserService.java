package com.cody.domain.store.cache.service.redis;

import static com.cody.domain.store.cache.constants.constants.ALL_USER;

import com.cody.domain.store.cache.dto.AllUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AllUserService {
    private final RedisTemplate<String, AllUser> redisAllUserTemplate;
    public AllUser getUser(long userId) {
        return redisAllUserTemplate.opsForValue().get(String.format(ALL_USER, userId));
    }
    public void addUsers(List<AllUser> users) {
        redisAllUserTemplate.executePipelined((RedisCallback<AllUser>) connection -> {
            RedisSerializer keySerializer = redisAllUserTemplate.getKeySerializer();
            RedisSerializer valueSerializer = redisAllUserTemplate.getValueSerializer();
                users.forEach(user -> {
                    byte[] key = keySerializer.serialize(ALL_USER + user.getUserId());
                    byte[] value = valueSerializer.serialize(user);
                    connection.set(key, value);
                });
               return null;
        });
    }
}
