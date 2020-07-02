package com.frzcd.ftpproducer.config;

import com.frzcd.ftpproducer.exceptions.MyKafkaServerConnectionException;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.admin.KafkaAdminClient;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;

import static com.frzcd.ftpproducer.utils.LogMessages.KAFKA_PRODUCER_CONFIG_INFO_4;
import static com.frzcd.ftpproducer.utils.LogMessages.KAFKA_SERVER_CONFIG_ERROR_3;

@Slf4j
@Configuration
public class KafkaProducerConfig {

    @Value("${spring.kafka.bootstrap-servers}")
    private String kafkaServer;

//    @Bean
//    public KafkaTemplate<String, Bytes> kafkaTemplateBytes() {
//        return new KafkaTemplate<>(producerFactoryBytes());
//    }

    @Bean
    public KafkaTemplate<String, String> kafkaTemplateStrings() {
        return new KafkaTemplate<>(producerFactoryStrings());
    }

//    @Bean
//    ProducerFactory<String, Bytes> producerFactoryBytes() {
//        checkKafkaServerConnection(producerConfigsBytes());
//        return new DefaultKafkaProducerFactory<>(producerConfigsBytes());
//    }

    @Bean
    ProducerFactory<String, String> producerFactoryStrings() {
        checkKafkaServerConnection(producerConfigsStrings());
        return new DefaultKafkaProducerFactory<>(producerConfigsStrings());
    }

//    @Bean
//    public Map<String, Object> producerConfigsBytes() {
//        Map<String, Object> props = new HashMap<>();
//
//        props.put(
//                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
//                kafkaServer);
//        props.put(
//                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
//                StringSerializer.class);
//        props.put(
//                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
//                BytesSerializer.class);
//
//        return props;
//    }

    @Bean
    public Map<String, Object> producerConfigsStrings() {
        Map<String, Object> props = new HashMap<>();

        props.put(
                ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                kafkaServer);
        props.put(
                ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                StringSerializer.class);
        props.put(
                ProducerConfig.MAX_REQUEST_SIZE_CONFIG,
                4000000);

        return props;
    }

    private void checkKafkaServerConnection(Map<String, Object> props) {
        try {
            System.out.println(KafkaAdminClient.create(producerConfigsStrings()).listTopics().listings().get());
            log.info(KAFKA_PRODUCER_CONFIG_INFO_4);
        } catch (InterruptedException | ExecutionException e) {
            log.error(KAFKA_SERVER_CONFIG_ERROR_3);
            e.printStackTrace();
            throw new MyKafkaServerConnectionException();
        }
    }
}
