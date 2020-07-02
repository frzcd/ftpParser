package com.frzcd.ftpproducer.service.sender;

import com.frzcd.ftpproducer.domain.MyFile;

import java.io.File;

public interface Sender {
    public void send(MyFile fileToSend);
//    public void send(File file);
//    public void send(String stringToSend);
}
