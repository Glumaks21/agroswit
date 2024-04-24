package ua.com.agroswit.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtConfigProperties(
        String secret,
        int accessExpiration,
        int refreshExpiration
) {
}
