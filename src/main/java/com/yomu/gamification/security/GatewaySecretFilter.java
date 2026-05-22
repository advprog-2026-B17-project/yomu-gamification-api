package com.yomu.gamification.security;

import com.yomu.gamification.config.AppConfig;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Gateway secret filter that validates X-Gateway-Secret header for inter-service authentication.
 * Extracts and forwards user identity headers as request attributes.
 */
@Component
public class GatewaySecretFilter extends OncePerRequestFilter {

    private static final String GATEWAY_SECRET_HEADER = "X-Gateway-Secret";
    private static final String USER_ID_HEADER = "X-User-Id";
    private static final String USER_ROLE_HEADER = "X-User-Role";
    private static final String USERNAME_HEADER = "X-Username";

    private static final String ATTR_USER_ID = "authenticated.userId";
    private static final String ATTR_USER_ROLE = "authenticated.userRole";
    private static final String ATTR_USERNAME = "authenticated.username";
    private static final String ATTR_USER_ID_SHORT = "userId";

    private final AppConfig appConfig;

    public GatewaySecretFilter(AppConfig appConfig) {
        this.appConfig = appConfig;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String path = request.getRequestURI();

        // Public endpoints
        if (isPublicPath(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Protected endpoints require valid gateway secret
        if (!hasValidGatewaySecret(request)) {
            response.setStatus(HttpServletResponse.SC_FORBIDDEN);
            response.getWriter().write("Forbidden: Invalid or missing gateway secret");
            return;
        }

        // Extract and forward user identity headers as request attributes
        String userId = request.getHeader(USER_ID_HEADER);
        String userRole = request.getHeader(USER_ROLE_HEADER);
        String username = request.getHeader(USERNAME_HEADER);

        if (StringUtils.hasText(userId)) {
            request.setAttribute(ATTR_USER_ID, userId);
            request.setAttribute(ATTR_USER_ID_SHORT, userId);
        }
        if (StringUtils.hasText(userRole)) {
            request.setAttribute(ATTR_USER_ROLE, userRole);
        }
        if (StringUtils.hasText(username)) {
            request.setAttribute(ATTR_USERNAME, username);
        }

        filterChain.doFilter(request, response);
    }

    private boolean isPublicPath(String path) {
        return path.startsWith("/actuator/health");
    }

    private boolean hasValidGatewaySecret(HttpServletRequest request) {
        String secret = appConfig.getGateway().getSecret();
        if (!StringUtils.hasText(secret)) {
            // No secret configured, allow all requests (development mode)
            return true;
        }
        String providedSecret = request.getHeader(GATEWAY_SECRET_HEADER);
        return secret.equals(providedSecret);
    }
}