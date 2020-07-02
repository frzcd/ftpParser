package com.frzcd.ftpproducer.service.initializer;

import com.frzcd.ftpproducer.service.parser.FtpFileParser;
import com.frzcd.ftpproducer.service.parser.Parser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;

@Slf4j
@Service
public class Initializer implements Initializable {
    ExecutorService executorService;
    Parser parser;

    public Initializer(ExecutorService executorService,
                       Parser parser) {
        this.executorService = executorService;
        this.parser = parser;
    }

    @Override
    public void init() {
        //executorService.submit(parser);
        log.warn(" empty init");
    }
}
