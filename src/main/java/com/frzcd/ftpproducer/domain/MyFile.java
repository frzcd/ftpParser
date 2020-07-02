package com.frzcd.ftpproducer.domain;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;

import java.util.List;

@Data
@Slf4j
public class MyFile {
    String fullFileName;
    String timestamp;
    String fileInfo;
    List<MyXmlFile> xmlFilesList;

    public MyFile(String fullFileName, String timestamp, List<MyXmlFile> xmlFilesList) {
        this.fullFileName = fullFileName;
        this.timestamp = timestamp;
        this.fileInfo = fullFileName + " " + timestamp;
        this.xmlFilesList = xmlFilesList;
        log.info("new MyFile created");
    }
}
