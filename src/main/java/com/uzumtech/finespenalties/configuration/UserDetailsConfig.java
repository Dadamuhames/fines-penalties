package com.uzumtech.finespenalties.configuration;

import com.uzumtech.finespenalties.constant.enums.Role;
import com.uzumtech.finespenalties.service.intr.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Configuration
public class UserDetailsConfig {

    @Bean(name = "customUserDetailServices")
    public Map<Role, CustomUserDetailsService> customUserDetailServices(List<CustomUserDetailsService> detailsServices) {
        Map<Role, CustomUserDetailsService> detailServiceMap = new HashMap<>();

        for (var detailsService : detailsServices) {
            detailServiceMap.put(detailsService.getSupportedRole(), detailsService);
        }

        return detailServiceMap;
    }
}
