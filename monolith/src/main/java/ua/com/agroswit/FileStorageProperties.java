package ua.com.agroswit;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "file-storage")
@Getter
@Setter
public class FileStorageProperties {
    private String uploadDir;
}
