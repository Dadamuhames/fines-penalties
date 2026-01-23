package com.uzumtech.finespenalties.configuration.property;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "services.gcp")
public class GcpServiceProperties {
    private String url;

    private String login;

    private String password;
}
