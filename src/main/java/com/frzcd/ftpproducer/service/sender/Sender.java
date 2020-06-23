package com.frzcd.ftpproducer.service.sender;

import com.frzcd.ftpproducer.domain.MyFile;

public interface Sender {
    public void send(MyFile fileToSend);
}
