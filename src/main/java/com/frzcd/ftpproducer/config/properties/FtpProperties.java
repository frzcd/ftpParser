package com.frzcd.ftpproducer.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties("ftp")
public class FtpProperties {
    private String server;
    private int port;
    private String user;
    private String password;

    private String[] directories;
}
