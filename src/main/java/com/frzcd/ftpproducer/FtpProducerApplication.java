package com.frzcd.ftpproducer;

import com.frzcd.ftpproducer.service.initializer.Initializable;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.kafka.annotation.KafkaListener;

import java.util.List;

@Slf4j
@SpringBootApplication
@AllArgsConstructor
public class FtpProducerApplication implements ApplicationRunner {

	private final List<Initializable> initializableList;

	@KafkaListener(topics="${topics}")
	public void testListener(ConsumerRecord<String, String> record) {
		log.info("record.partition: " + record.partition());
		log.info("record.key: " + record.key());
//		System.out.println(record.value());
	}

	public static void main(String[] args) {
		SpringApplication.run(FtpProducerApplication.class, args);
	}

	@Override
	public void run(ApplicationArguments args) {
		initializableList.forEach(Initializable::init);
	}
}
