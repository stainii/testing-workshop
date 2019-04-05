package be.stijnhooft.testing.demo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.client.RestTemplate;

/**
 * Run this class to start up the application.
 * Prerequisites:
 *  - Have Java 11 or higher installed on your machine.
 *  - Port 8080 must be unused before starting this app
 */
@SpringBootApplication
public class TestingDemoApplication {

    public static void main(String[] args) {
        SpringApplication.run(TestingDemoApplication.class, args);
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

}
