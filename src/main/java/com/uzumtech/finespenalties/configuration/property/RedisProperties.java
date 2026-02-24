package com.uzumtech.finespenalties.configuration.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Setter
@Getter
@Configuration
@ConfigurationProperties(prefix = "spring.data.redis")
public class RedisProperties {
    String host;

    int port;

    int database;

    String username;

    String password;

    int defaultTtl;

    int timeout;

    int shutdownTimeout;

    int orderTtl;

    int merchantTtl;
}
