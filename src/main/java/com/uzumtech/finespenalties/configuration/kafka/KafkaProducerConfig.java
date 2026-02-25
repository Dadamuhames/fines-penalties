package com.uzumtech.finespenalties.configuration.kafka;

import com.uzumtech.finespenalties.configuration.property.KafkaProperties;
import com.uzumtech.finespenalties.dto.event.NotificationEvent;
import com.uzumtech.finespenalties.dto.event.OffenseEvent;
import com.uzumtech.finespenalties.dto.event.OtpSendEvent;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.ByteArraySerializer;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.serializer.DelegatingByTypeSerializer;
import org.springframework.kafka.support.serializer.JacksonJsonSerializer;

import java.util.HashMap;
import java.util.Map;

@Configuration
@RequiredArgsConstructor
public class KafkaProducerConfig {
    private final KafkaProperties kafkaProperties;

    @Bean
    public Map<String, Object> producerConfigs() {
        Map<String, Object> props = new HashMap<>();

        String bootstrapServers = kafkaProperties.getProducer().getBootstrapServers();

        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JacksonJsonSerializer.class);

        return props;
    }


    @Bean
    public KafkaTemplate<String, Object> kafkaTemplate() {
        ProducerFactory<String, Object> factory = new DefaultKafkaProducerFactory<>(producerConfigs(), new StringSerializer(),
            new DelegatingByTypeSerializer(
                Map.of(
                    byte[].class, new ByteArraySerializer(),
                    NotificationEvent.class, new JacksonJsonSerializer<>(),
                    OffenseEvent.class, new JacksonJsonSerializer<>()
                )));

        return new KafkaTemplate<>(factory);
    }
}
