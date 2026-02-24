package com.uzumtech.finespenalties.configuration;

import com.uzumtech.finespenalties.configuration.property.RedisProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.JacksonJsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;

@Configuration
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties props;

    @Bean
    public LettuceConnectionFactory redisConnectionFactory() {
        var configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(props.getHost());
        configuration.setPort(props.getPort());
        configuration.setPassword(props.getPassword());
        configuration.setDatabase(props.getDatabase());

        var lettuceClientConfiguration = LettuceClientConfiguration
            .builder()
            .commandTimeout(Duration.ofMillis(props.getTimeout()))
            .shutdownTimeout(Duration.ofMillis(props.getShutdownTimeout()))
            .build();

        return new LettuceConnectionFactory(configuration, lettuceClientConfiguration);
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate(
        RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(redisConnectionFactory);

        JacksonJsonRedisSerializer<Object> serializer =
            new JacksonJsonRedisSerializer<>(Object.class);

        template.setDefaultSerializer(serializer);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(serializer);
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(serializer);

        template.afterPropertiesSet();
        return template;
    }
}
