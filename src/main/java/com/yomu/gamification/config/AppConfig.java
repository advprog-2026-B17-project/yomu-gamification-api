package com.yomu.gamification.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app")
public class AppConfig {

    private GatewayConfig gateway = new GatewayConfig();

    public GatewayConfig getGateway() {
        return gateway;
    }

    public void setGateway(GatewayConfig gateway) {
        this.gateway = gateway;
    }

    public static class GatewayConfig {
        private String secret;

        public String getSecret() {
            return secret;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }
    }
}