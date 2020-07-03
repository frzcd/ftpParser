package com.frzcd.ftpproducer.domain;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Data
@Slf4j
public class MyFile {
    private String fullFileName;
    private String timestamp;
    private String fileInfo;
    private List<MyXmlFile> xmlFilesList;

    public MyFile(String fullFileName, String timestamp, List<MyXmlFile> xmlFilesList) {
        this.fullFileName = fullFileName;
        this.timestamp = timestamp;
        this.fileInfo = fullFileName + " " + timestamp;
        this.xmlFilesList = xmlFilesList;
    }
}
