package ua.com.agroswit.productservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class Config {

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }

}
