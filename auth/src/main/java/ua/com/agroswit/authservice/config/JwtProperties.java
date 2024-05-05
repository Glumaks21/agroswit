package ua.com.agroswit.authservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "jwt")
public record JwtProperties(
        String secret,
        Integer accessExpiration,
        Integer refreshExpiration
) {
}
