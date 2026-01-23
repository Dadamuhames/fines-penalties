package com.uzumtech.finespenalties.configuration.property;


import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "spring.security.cors")
public class CorsProperties {
    private String allowedOrigins;

    private String allowedMethods;

    private String allowedHeaders;

    public List<String> getAllowedOriginsList() {
        String[] origins = allowedOrigins.split(",");

        return Arrays.asList(origins);
    }
}

