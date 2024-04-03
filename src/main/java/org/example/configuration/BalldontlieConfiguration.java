package org.example.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class BalldontlieConfiguration {

    @Value("${balldontlie.url}")
    private String ballDontLieUrl;
    @Value("${balldontlie.api-key}")
    private String apiKey;

    @Bean
    public RestTemplate getBallDontLieRestTemplate(RestTemplateBuilder builder) {
       return builder
               .rootUri(ballDontLieUrl)
               .defaultHeader("Authorization", apiKey)
               .build();
    }
}
