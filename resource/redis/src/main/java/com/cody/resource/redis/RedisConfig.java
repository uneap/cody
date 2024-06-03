package com.cody.resource.redis;

import io.lettuce.core.ReadFrom;
import io.lettuce.core.cluster.ClusterClientOptions;
import io.lettuce.core.cluster.ClusterTopologyRefreshOptions;
import java.time.Duration;
import lombok.RequiredArgsConstructor;
import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisClusterConfiguration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettucePoolingClientConfiguration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.util.StringUtils;

@Configuration
@RequiredArgsConstructor
public class RedisConfig {
    private final ClusterConfigurationProperties clusterConfigurationProperties;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        ClusterTopologyRefreshOptions clusterTopologyRefreshOptions =
            ClusterTopologyRefreshOptions.builder()
                                         .enablePeriodicRefresh(Duration.ofSeconds(60))
                                         .enableAllAdaptiveRefreshTriggers()
                                         .build();

        ClusterClientOptions clusterClientOptions =
            ClusterClientOptions.builder()
                                .validateClusterNodeMembership(false)
                                .topologyRefreshOptions(clusterTopologyRefreshOptions)
                                .build();


        LettucePoolingClientConfiguration lettuceConfiguration = LettucePoolingClientConfiguration.builder()
                                                                                                  .poolConfig(poolConfig())
                                                                                                  .clientOptions(clusterClientOptions)
                                                                                                  .readFrom(
                                                                                                      ReadFrom.MASTER_PREFERRED)
                                                                                                  .build();

        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(clusterConfiguration(), lettuceConfiguration);
        return lettuceConnectionFactory;
    }

    @Bean
    public GenericObjectPoolConfig poolConfig() {
        GenericObjectPoolConfig poolConfig = new GenericObjectPoolConfig();
        poolConfig.setMaxWaitMillis(2 * 1000);
        poolConfig.setMaxTotal(100);
        poolConfig.setMaxIdle(20);
        poolConfig.setMinIdle(20);

        return poolConfig;
    }

    @Bean
    public RedisTemplate<String, String> redisCommonStringTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(redisConnectionFactory());
        return redisTemplate;
    }

    private RedisClusterConfiguration clusterConfiguration() {
        RedisClusterConfiguration redisClusterConfiguration = new RedisClusterConfiguration(clusterConfigurationProperties.getNodes());
        redisClusterConfiguration.setMaxRedirects(clusterConfigurationProperties.getMaxRedirects());
        String password = clusterConfigurationProperties.getPassword();
        if (!StringUtils.isEmpty(password)) {
            redisClusterConfiguration.setPassword(clusterConfigurationProperties.getPassword());
        }

        return redisClusterConfiguration;
    }

}
