package com.frzcd.ftpproducer.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("kafka")
public class KafkaProperties {
    private String topic;
    private String bootstrapServer;
    private int maxRequestSizeConfig;
}
