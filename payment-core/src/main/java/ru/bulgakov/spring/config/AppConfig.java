package ru.bulgakov.spring.config;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import ru.bulgakov.spring.handler.RestTemplateResponseErrorHandler;

@Configuration
@RequiredArgsConstructor
@EnableConfigurationProperties(RestTemplateClientConfigurationProperties.class)
public class AppConfig {
    private final RestTemplateClientConfigurationProperties clientConfigurationProperties;
    private final RestTemplateResponseErrorHandler errorHandler;

    @Bean
    public RestTemplate productClient() {
        RestTemplateProperties productClient = clientConfigurationProperties.getProductClient();
        return new RestTemplateBuilder()
                .rootUri(productClient.getUrl())
                .readTimeout(productClient.getReadTimeout())
                .connectTimeout(productClient.getConnectTimeout())
                .errorHandler(errorHandler)
                .build();
    }
}
