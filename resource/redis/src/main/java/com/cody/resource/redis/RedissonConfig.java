package com.cody.resource.redis;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.redisson.Redisson;
import org.redisson.api.RedissonClient;
import org.redisson.client.codec.StringCodec;
import org.redisson.config.Config;
import org.redisson.spring.data.connection.RedissonConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.CollectionUtils;

@Configuration
@RequiredArgsConstructor
public class RedissonConfig {

    private final ClusterConfigurationProperties clusterConfigurationProperties;

    @Bean
    public RedissonConnectionFactory redissonConnectionFactory(RedissonClient redisson) {
        return new RedissonConnectionFactory(redisson);
    }

    @Bean(destroyMethod = "shutdown")
    public RedissonClient redisson() throws IOException {
        Config config = new Config();
        List<String> nodes = new ArrayList<>();
        if (!CollectionUtils.isEmpty(clusterConfigurationProperties.getNodes())) {
            nodes = clusterConfigurationProperties.getNodes().stream().map(nodeStr -> {
                if (!nodeStr.startsWith("redis://") && !nodeStr.startsWith("redis://")) {
                    return "redis://" + nodeStr;
                }
                return nodeStr;
            }).collect(Collectors.toList());
        }

        config.setCodec(StringCodec.INSTANCE)
                .useClusterServers()
                .setPassword(clusterConfigurationProperties.getPassword())
                .setNodeAddresses(nodes);


        return Redisson.create(config);
    }
}
