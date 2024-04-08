package ua.com.agroswit.inventoryservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class BeanConfig {

    @LoadBalanced
    @Bean
    WebClient.Builder restClient() {
        return WebClient.builder();
    }

}
