package com.yomu.gamification.config;

import com.yomu.gamification.security.GatewaySecretFilter;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SecurityConfig {

    private final GatewaySecretFilter gatewaySecretFilter;

    public SecurityConfig(GatewaySecretFilter gatewaySecretFilter) {
        this.gatewaySecretFilter = gatewaySecretFilter;
    }

    @Bean
    public FilterRegistrationBean<GatewaySecretFilter> gatewaySecretFilterRegistration() {
        FilterRegistrationBean<GatewaySecretFilter> registration = new FilterRegistrationBean<>();
        registration.setFilter(gatewaySecretFilter);
        registration.addUrlPatterns("/*");
        registration.setOrder(-1); // Run before Spring Security filters
        registration.setName("gatewaySecretFilter");
        return registration;
    }
}