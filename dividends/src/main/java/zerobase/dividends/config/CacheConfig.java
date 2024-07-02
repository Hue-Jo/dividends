package zerobase.dividends.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.CacheManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
@RequiredArgsConstructor
public class CacheConfig {

    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;


    @Bean
    public RedisConnectionFactory redisConnectionFactory() {

        RedisStandaloneConfiguration config = new RedisStandaloneConfiguration();
        config.setHostName(this.host);
        config.setPort(this.port);
        return new LettuceConnectionFactory(config);
    }


    @Bean
    public CacheManager redisCacheManager(RedisConnectionFactory redisConnectionFactory) {

        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext
                                    .SerializationPair
                                    .fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext
                                    .SerializationPair
                                    .fromSerializer(new GenericJackson2JsonRedisSerializer()));
                //.entryTtl(Duration.of())


        return RedisCacheManager.RedisCacheManagerBuilder
                                .fromConnectionFactory(redisConnectionFactory)
                                .cacheDefaults(config)
                                .build();
    }
}
