package ua.com.agroswit.inventoryservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class BeanConfig {

    @LoadBalanced
    @Bean
    RestClient.Builder restClient() {
        return RestClient.builder();
    }

}
