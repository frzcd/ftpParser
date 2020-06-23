package com.frzcd.ftpproducer.service.sender;

import com.frzcd.ftpproducer.domain.MyFile;
import com.frzcd.ftpproducer.service.sender.Sender;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.utils.Bytes;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

import static com.frzcd.ftpproducer.utils.LogMessages.*;

@Slf4j
@Service
public class FtpFileSender implements Sender {
    private KafkaTemplate<String, Bytes> kafkaTemplateBytes;

    public FtpFileSender(KafkaTemplate<String, Bytes> kafkaTemplateBytes) {
        this.kafkaTemplateBytes = kafkaTemplateBytes;
        log.info(FTP_FILE_SENDER_INFO_16);
    }

    public void send(MyFile fileToSand) {
        String fileFullName = fileToSand.getFullFileName();
        Bytes data = fileToSand.getData();

        log.info(FTP_FILE_SENDER_INFO_17, fileFullName);

        ListenableFuture<SendResult<String, Bytes>> future =
                kafkaTemplateBytes.send("test", fileFullName, data);

        future.addCallback(new ListenableFutureCallback<SendResult<String, Bytes>>() {
            @Override
            public void onFailure(Throwable throwable) {
                log.error(FTP_FILE_SENDER_ERROR_8, throwable.getLocalizedMessage());
                throwable.printStackTrace();
            }

            @Override
            public void onSuccess(SendResult<String, Bytes> stringBytesSendResult) {
                log.info(FTP_FILE_SENDER_INFO_18, fileFullName);
            }
        });
    }
}
