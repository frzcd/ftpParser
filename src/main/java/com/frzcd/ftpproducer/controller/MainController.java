package com.frzcd.ftpproducer.controller;

import com.frzcd.ftpproducer.service.parser.Parser;
import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.concurrent.ExecutorService;

@RestController
@RequestMapping
public class MainController {
    @Autowired
    FTPClient ftp;

    @Autowired
    Parser parser;
    @Autowired
    ExecutorService executorService;


    @GetMapping
    public String newCircle() {
        executorService.submit(parser);
        return "get rest";
    }

    @GetMapping("/del")
    public String del() {
        return "empty method";
    }
}
