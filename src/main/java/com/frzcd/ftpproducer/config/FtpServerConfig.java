package com.frzcd.ftpproducer.config;

import com.frzcd.ftpproducer.config.properties.FtpProperties;
import com.frzcd.ftpproducer.exceptions.MyFtpConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Configuration
public class FtpServerConfig {
    private final String server;
    private final int port;
    private final String user;
    private final String pass;

    public FtpServerConfig(FtpProperties ftpProperties) {
        this.server = ftpProperties.getServer();
        this.port = ftpProperties.getPort();
        this.user = ftpProperties.getUser();
        this.pass = ftpProperties.getPassword();
    }

    @Bean
    public FTPClient ftp() {
        log.info(FTP_SERVER_CONFIG_INFO_1, server);

        FTPClient ftp = new FTPClient();

        try {
            ftp.connect(server, port);
            showServerReply(ftp);
            int replyCode = ftp.getReplyCode();

            if (!FTPReply.isPositiveCompletion(replyCode)) {
                log.error(FTP_SERVER_CONFIG_ERROR_1, replyCode);
                throw new MyFtpConnectionException();
            }

            ftp.login(user, pass);
            log.info(FTP_SERVER_CONFIG_INFO_3);

        } catch (IOException e) {
            log.error(FTP_SERVER_CONFIG_ERROR_2);
            throw new MyFtpConnectionException();
        }

        ftp.enterLocalPassiveMode();

        return ftp;
    }

    private void showServerReply(FTPClient ftp) {
        String[] replies = ftp.getReplyStrings();

        if (replies != null && replies.length > 0) {
            for (String reply : replies) {
                log.info(FTP_SERVER_CONFIG_INFO_2, reply);
            }
        }
    }
}
