package com.frzcd.ftpproducer.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Data
@Configuration
@ConfigurationProperties("app")
public class AppProperties {
    String saveFile;
    String lastUpdateFile;
    String cron;
}
