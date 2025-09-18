package ru.bulgakov.spring.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Getter
@RequiredArgsConstructor
@ConfigurationProperties("integrations.clients")
public class RestTemplateClientConfigurationProperties {

    private final RestTemplateProperties productClient;
}
