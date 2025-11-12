package com.example.portfoliospring1.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;


@Configuration
@EnableFeignClients(basePackages = "com.example.portfoliospring1")
public class Feignconfig {
}
