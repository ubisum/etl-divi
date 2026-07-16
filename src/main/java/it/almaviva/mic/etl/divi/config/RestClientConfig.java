package it.almaviva.mic.etl.divi.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {

    @Bean
    public RestClient diviRestClient() {
        return RestClient.builder().build();
    }
}