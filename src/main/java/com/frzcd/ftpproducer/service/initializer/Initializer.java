package com.frzcd.ftpproducer.service.initializer;

import com.frzcd.ftpproducer.service.parser.FtpFileParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class Initializer {
    ExecutorService executorService;
    FtpFileParser parser;

    public Initializer(ExecutorService executorService,
                       FtpFileParser parser) {
        this.executorService = executorService;
        this.parser = parser;

        log.info("initializer constructor");
    }

    @PostConstruct
    public void init() {
        log.info("init postconstruct");
        executorService.submit(parser);
    }
}
