package com.cody.domain.store.cache.service.redis;

import static com.cody.domain.store.cache.constants.constants.ALL_USER;

import com.cody.domain.store.cache.dto.AllUser;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AllUserService {
    private final RedisTemplate<String, AllUser> redisAllUserTemplate;
    public AllUser getUser(long userId) {
        return redisAllUserTemplate.opsForValue().get(String.format(ALL_USER, userId));
    }
    public void addUsers(List<AllUser> users) {
        redisAllUserTemplate.executePipelined(new SessionCallback() {
            @Override
            public Object execute(RedisOperations operations) throws DataAccessException {
                users.forEach(user -> {
                    operations.opsForValue().set(ALL_USER + user.getUserId(), user);
                });
                return operations.exec();
            }
        });
    }
}
