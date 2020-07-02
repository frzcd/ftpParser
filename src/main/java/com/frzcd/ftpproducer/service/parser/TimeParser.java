package com.frzcd.ftpproducer.service.parser;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class TimeParser extends TimerTask {
    Parser parser;
    ExecutorService executorService;

    @Autowired
    public TimeParser(Parser parser, ExecutorService executorService) {
        this.parser = parser;
        this.executorService = executorService;
    }

    @PostConstruct
    public void action() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(1990, Calendar.FEBRUARY, 1);
        Timer timer = new Timer();
        timer.schedule(this, 0L, 20L * 1000L);
    }

    @Override
    public void run() {
        log.warn("NEW");
        executorService.submit(parser);
    }
}
