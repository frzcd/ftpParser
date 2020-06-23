package com.frzcd.ftpproducer;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;
import java.util.Arrays;

@Slf4j
@SpringBootApplication
public class FtpProducerApplication {

	@KafkaListener(topics="${topics}")
	public void testListener(ConsumerRecord<String, String> record) {
		log.info("record.partition: " + record.partition());
		log.info("record.key: " + record.key());
//		System.out.println(record.value());
	}

	public static void main(String[] args) {
		SpringApplication.run(FtpProducerApplication.class, args);
	}
}
