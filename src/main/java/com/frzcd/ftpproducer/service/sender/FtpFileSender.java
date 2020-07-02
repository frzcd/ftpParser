package com.frzcd.ftpproducer.service.sender;

import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.domain.MyXmlFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;
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
    private KafkaTemplate<String, String> kafkaTemplateStrings;

    @Autowired
    public FtpFileSender(KafkaTemplate<String, String> kafkaTemplateStrings) {
        this.kafkaTemplateStrings = kafkaTemplateStrings;
        log.info(FTP_FILE_SENDER_INFO_16);
    }

    public void send(MyFile myFile) {
        String name = myFile.getFullFileName();

        int xmlsSand = 0;

        for (MyXmlFile xmlFile : myFile.getXmlFilesList()) {
            xmlsSand++;

            log.info("{} trying to send", xmlsSand);

            try {
                Thread.sleep(2);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            ListenableFuture<SendResult<String, String>> future =
                    kafkaTemplateStrings.send("test", name + " " + xmlsSand, xmlFile.getData());

            future.addCallback(new ListenableFutureCallback<SendResult<String, String>>() {

                @Override
                public void onFailure(Throwable throwable) {
                    log.error(FTP_FILE_SENDER_ERROR_8, throwable.getLocalizedMessage());
                    throwable.printStackTrace();
                }

                @Override
                public void onSuccess(SendResult<String, String> stringStringSendResult) {
                    log.info(FTP_FILE_SENDER_INFO_18, name);
                }
            });
        }
        log.info("SENT");
    }

    @PostConstruct
    public void testSend() {
        kafkaTemplateStrings.send("test", "Some test message sand from SenderImpl");
    }
}
