package com.uzumtech.finespenalties.configuration;

import com.uzumtech.finespenalties.configuration.property.CorsProperties;
import com.uzumtech.finespenalties.filter.JwtAuthFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration {
    private final JwtAuthFilter jwtAuthFilter;
    private final CorsProperties corsProperties;

    @Bean
    protected PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    protected AuthenticationManager authenticationManager(
        AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    protected SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http.securityMatcher("/api/**")
            .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
            .authorizeHttpRequests(
                auth ->
                    auth.requestMatchers(
                            "/api/v1/fines-penalties/inspector/auth/**",
                            "/api/v1/fines-penalties/user/auth/**",
                            "/api/v1/fines-penalties/court/penalty-webhook",
                            "/api/v1/fines-penalties/common/**",
                            "/api/v1/fines-penalties/notification-service/callback",
                            "/api/v1/fines-penalties/user/otp/requesting-by-pinfl"
                        ).permitAll()
                        .requestMatchers("/api/v1/fines-penalties/inspector/**")
                        .hasRole("INSPECTOR")
                        .requestMatchers("/api/v1/fines-penalties/user/**")
                        .hasRole("USER"))
            .csrf(AbstractHttpConfigurer::disable)
            .httpBasic(AbstractHttpConfigurer::disable)
            .formLogin(AbstractHttpConfigurer::disable)
            .anonymous(AbstractHttpConfigurer::disable)
            .cors(cors -> cors.configurationSource(corsConfigurationSource()));

        return http.build();
    }

    @Bean
    protected CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowCredentials(true);
        configuration.setAllowedOrigins(corsProperties.getAllowedOriginsList());
        configuration.addAllowedMethod(corsProperties.getAllowedMethods());
        configuration.addAllowedHeader(corsProperties.getAllowedHeaders());
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
