package com.frzcd.ftpproducer.service.initializer;

import com.frzcd.ftpproducer.service.parser.FtpFileParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class Initializer implements Initializable {
    ExecutorService executorService;
    FtpFileParser parser;

    public Initializer(ExecutorService executorService,
                       FtpFileParser parser) {
        this.executorService = executorService;
        this.parser = parser;
    }

    @Override
    public void init() {
        executorService.submit(parser);
    }
}
