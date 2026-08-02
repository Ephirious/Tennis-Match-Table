package com.ephirious.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

@org.springframework.context.annotation.Configuration
@EnableWebMvc
@ComponentScan(basePackages = {"com.ephirious.controller", "com.ephirious.handler"})
public class WebConfig {

    @Bean
    public LocalValidatorFactoryBean validator() {
        return new LocalValidatorFactoryBean();
    }
}
