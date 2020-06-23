package com.frzcd.ftpproducer.domain;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;

@Data
@Slf4j
public class MyFile {
    String fullFileName;
    String timestamp;
    String fileInfo;
    Bytes data;

    public MyFile(String fullFileName, String timestamp, Bytes data) {
        this.fullFileName = fullFileName;
        this.timestamp = timestamp;
        this.data = data;
        this.fileInfo = fullFileName + " " + timestamp;
    }
}
