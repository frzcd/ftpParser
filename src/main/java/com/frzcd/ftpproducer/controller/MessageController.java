package com.frzcd.ftpproducer.controller;

import org.apache.commons.net.ftp.FTPClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.*;

@RestController
@RequestMapping("message")
public class MessageController {

    private KafkaTemplate<Long, String> kafkaTemplate;
    private FTPClient ftp;

    private Long messageId;

    public MessageController(@Autowired KafkaTemplate<Long, String> kafkaTemplate,
                             @Autowired FTPClient ftp) {
        this.kafkaTemplate = kafkaTemplate;
        this.ftp = ftp;
        this.messageId = 1L;
    }

    @PostMapping
    public void sendMessage(String message) {

        ListenableFuture<SendResult<Long, String>> future = kafkaTemplate.send("test", messageId, message);
        messageId++;

        future.addCallback(System.out::println, System.err::println);

        kafkaTemplate.flush();
    }

    @PutMapping
        public void sendFtpMessage() {

        ListenableFuture<SendResult<Long, String>> future =
                kafkaTemplate.send("test", messageId, "message from Put");
        messageId++;

        future.addCallback(System.out::println, System.err::println);

        kafkaTemplate.flush();
    }

    public String readFile(String path) {

        int reply = ftp.getReplyCode();
        int len;
        String result = "";
        byte[] buffer = new byte[1024];

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             InputStream inputStream = ftp.retrieveFileStream(path)) {

            while ((len = inputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, len);
            }
            result = byteArrayOutputStream.toString();
        } catch (IOException e) {
            System.out.println("IOException in readFile");
        }

        return result;
    }
}
