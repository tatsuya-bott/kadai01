package com.japan.compass.annotation.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration(proxyBeanMethods = false)
public class ApplicationConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("Asia/Tokyo"));
    }
}
