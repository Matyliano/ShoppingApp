package com.example.shopping.configuration;

import com.example.shopping.dto.ProductDto;
import com.example.shopping.dto.UserDto;
import com.example.shopping.entity.Product;
import com.example.shopping.entity.User;
import com.example.shopping.mapper.history.ProductHistoryMapper;
import com.example.shopping.mapper.history.UserHistoryMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.history.Revision;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;

@Configuration
public class BeanConfiguration {
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public static LocalDateTime getDate() {
        return java.time.LocalDateTime.now();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    ProductHistoryMapper productHistoryMapper() {
        return new ProductHistoryMapper() {
            @Override
            public ProductDto toDto(Revision<Integer, Product> revision) {
                return null;
            }
        };
    }

    @Bean
    UserHistoryMapper userHistoryMapper() {
        return new UserHistoryMapper() {
            @Override
            public UserDto toDto(Revision<Integer, User> revision) {
                return null;
            }
        };
    }
}


