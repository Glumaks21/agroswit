package ua.com.agroswit.productservice.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "minio")
public record MinioProperties(
        String bucket,
        String endpoint,
        String accessKey,
        String secretKey
) {
}
