package com.example.shopping.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Configuration
public class BeanConfiguration {
    @Bean
    public RestTemplate restTemplate() {return new RestTemplate();}

    @Bean
    public static LocalDateTime getDate(){
        return java.time.LocalDateTime.now();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
