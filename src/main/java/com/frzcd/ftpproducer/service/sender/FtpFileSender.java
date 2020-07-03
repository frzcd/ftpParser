package com.frzcd.ftpproducer.service.sender;

import com.frzcd.ftpproducer.config.properties.KafkaProperties;
import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.domain.MyXmlFile;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import javax.annotation.PostConstruct;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Service
public class FtpFileSender implements Sender {
    private final KafkaTemplate<String, String> kafkaTemplateStrings;
    private final KafkaProperties kafkaProperties;

    @Autowired
    public FtpFileSender(KafkaTemplate<String, String> kafkaTemplateStrings, KafkaProperties kafkaProperties) {
        this.kafkaTemplateStrings = kafkaTemplateStrings;
        this.kafkaProperties = kafkaProperties;
        log.info(FTP_FILE_SENDER_INFO_16);
    }

    public void send(MyFile myFile) {
        String name = myFile.getFullFileName();

        for (MyXmlFile xmlFile : myFile.getXmlFilesList()) {

            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                log.error(FTP_FILE_SENDER_ERROR_10);
                e.printStackTrace();
            }

            ListenableFuture<SendResult<String, String>> future =
                    kafkaTemplateStrings.send(kafkaProperties.getTopic(), name, xmlFile.getData());

            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    log.error(FTP_FILE_SENDER_ERROR_8, throwable.getLocalizedMessage());
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    //log.info(FTP_FILE_SENDER_INFO_18, name);
                }
            });
        }
    }
}
