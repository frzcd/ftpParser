package com.frzcd.ftpproducer.service;

import com.frzcd.ftpproducer.service.parser.Parser;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;

@Slf4j
@Component
@AllArgsConstructor
public class ScheduleLauncher {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    Parser parser;
    ExecutorService executorService;

    @Scheduled(cron = "${app.cron}")
    public void scanStarter() {
        log.info("STARTING scan at {}", dateFormat.format(new Date()));
        executorService.submit(parser);
    }
}
