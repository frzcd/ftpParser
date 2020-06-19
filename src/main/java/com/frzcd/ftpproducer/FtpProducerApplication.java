package com.frzcd.ftpproducer;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.io.IOException;
import java.util.Arrays;

@SpringBootApplication
public class FtpProducerApplication {

	@KafkaListener(topics="${topics}")
	public void testListener(ConsumerRecord<String, String> record) {
		System.out.println(record.partition());
		System.out.println(record.key());
		System.out.println(record.value());
	}

	public static void main(String[] args) throws IOException {

		SpringApplication.run(FtpProducerApplication.class, args);
	}

}
