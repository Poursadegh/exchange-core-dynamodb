package exchange.core2.trading.config;

import exchange.core2.core.ExchangeCore;
import exchange.core2.core.ExchangeConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
public class TradingServiceConfig {

    @Bean
    public ExchangeCore exchangeCore() {
        ExchangeConfiguration config = ExchangeConfiguration.defaultBuilder()
                .build();
        return ExchangeCore.builder()
                .exchangeConfiguration(config)
                .build();
    }

    @Bean
    public RedisCacheManager cacheManager(RedisConnectionFactory connectionFactory) {
        RedisCacheConfiguration config = RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(10))
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .disableCachingNullValues();

        return RedisCacheManager.builder(connectionFactory)
                .cacheDefaults(config)
                .withCacheConfiguration("orders", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(1)))
                .withCacheConfiguration("trades", RedisCacheConfiguration.defaultCacheConfig()
                        .entryTtl(Duration.ofMinutes(5)))
                .build();
    }
} 