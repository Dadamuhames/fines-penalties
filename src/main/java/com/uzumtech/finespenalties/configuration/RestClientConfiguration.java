package com.uzumtech.finespenalties.configuration;

import com.uzumtech.finespenalties.configuration.property.service.CourtProperties;
import com.uzumtech.finespenalties.configuration.property.service.GcpServiceProperties;
import com.uzumtech.finespenalties.configuration.property.service.NotificationServiceProperties;
import com.uzumtech.finespenalties.handler.RestClientExceptionHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestClient;

import java.util.Base64;

@Configuration
@RequiredArgsConstructor
public class RestClientConfiguration {
    private final NotificationServiceProperties notificationServiceProperties;
    private final CourtProperties courtProperties;
    private final GcpServiceProperties gcpServiceProperties;

    @Bean(name = "notificationRestClient")
    public RestClient notificationRestClient(RestClient.Builder builder) {
        String authToken = getNotificationAuthToken();

        return builder.requestFactory(clientHttpRequestFactory())
            .defaultStatusHandler(new RestClientExceptionHandler())
            .baseUrl(notificationServiceProperties.getUrl())
            .defaultHeader("Authorization", String.format("Basic %s", authToken))
            .build();
    }

    private String getNotificationAuthToken() {
        String authTokenRaw = String.format("%s:%s", notificationServiceProperties.getLogin(), notificationServiceProperties.getPassword());

        return Base64.getEncoder().encodeToString(authTokenRaw.getBytes());
    }


    @Bean(name = "gcpRestClient")
    public RestClient gcpRestClient(RestClient.Builder builder) {
        return builder.requestFactory(clientHttpRequestFactory())
            .defaultStatusHandler(new RestClientExceptionHandler())
            .baseUrl(gcpServiceProperties.getUrl())
            .build();
    }

    @Bean(name = "courtRestClient")
    public RestClient courtRestClient(RestClient.Builder builder) {
        return builder.requestFactory(clientHttpRequestFactory())
            .defaultStatusHandler(new RestClientExceptionHandler())
            .baseUrl(courtProperties.getBaseUrl())
            .build();
    }

    @Bean
    public ClientHttpRequestFactory clientHttpRequestFactory() {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();

        factory.setConnectTimeout(5000);
        factory.setReadTimeout(1000);

        return factory;
    }
}
