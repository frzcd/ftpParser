package com.frzcd.ftpproducer.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Data
@Slf4j
@AllArgsConstructor
public class MyXmlFile {
    private String fileName;
    private String data;
}
