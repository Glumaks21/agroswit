package ua.com.agroswit.inventoryservice.config;

import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class BeanConfig {

    @Bean
    @LoadBalanced
    WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .baseUrl("http://product-service");
    }

    @Bean
    ProductClient productClient() {
        var proxyFactory = HttpServiceProxyFactory
                .builderFor(WebClientAdapter.create(webClientBuilder().build()))
                .build();
        return proxyFactory.createClient(ProductClient.class);
    }

}
