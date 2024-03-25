package ua.com.agroswit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestClient;

@SpringBootApplication
public class AgroswitApplication {

    public static void main(String[] args) {
        SpringApplication.run(AgroswitApplication.class, args);
    }

    @Bean
    RestClient restClient() {
        return RestClient.create();
    }
}
