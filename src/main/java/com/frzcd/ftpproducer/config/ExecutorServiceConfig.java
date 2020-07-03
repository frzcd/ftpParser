package com.frzcd.ftpproducer.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Configuration
public class ExecutorServiceConfig {

    @Bean
    ExecutorService executorService() {
        return Executors.newFixedThreadPool(1);
    }
}
