package com.yomu.gamification;

import com.yomu.gamification.config.AppConfig;
import com.yomu.gamification.security.GatewaySecretFilter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class GatewaySecretFilterTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AppConfig appConfig;

    private GatewaySecretFilter filter;

    @BeforeEach
    void setUp() {
        filter = new GatewaySecretFilter(appConfig);
    }

    @Test
    void actuatorHealthIsPublic() throws Exception {
        // /actuator/health should be accessible without gateway secret
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk());
    }

    @Test
    void apiRequestsWithoutGatewaySecretReturn403() throws Exception {
        // Configure a gateway secret
        appConfig.getGateway().setSecret("test-secret");

        // /api/** should return 403 without valid gateway secret
        mockMvc.perform(get("/api/test"))
                .andExpect(status().isForbidden());
    }

    @Test
    void apiRequestsWithValidGatewaySecretSucceed() throws Exception {
        // Configure a gateway secret
        appConfig.getGateway().setSecret("test-secret");

        // /api/** should succeed with valid gateway secret
        mockMvc.perform(get("/api/test")
                        .header("X-Gateway-Secret", "test-secret"))
                .andExpect(status().isOk());
    }

    @Test
    void apiRequestsWithInvalidGatewaySecretReturn403() throws Exception {
        // Configure a gateway secret
        appConfig.getGateway().setSecret("test-secret");

        // /api/** should return 403 with invalid gateway secret
        mockMvc.perform(get("/api/test")
                        .header("X-Gateway-Secret", "wrong-secret"))
                .andExpect(status().isForbidden());
    }
}