package com.uzumtech.finespenalties.component.kafka.publisher;

import com.uzumtech.finespenalties.constant.KafkaConstants;
import com.uzumtech.finespenalties.dto.event.OffenseEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OffenseEventPublisher implements EventPublisher<OffenseEvent> {

    private final KafkaTemplate<String, Object> kafkaTemplate;


    @Override
    public void publish(OffenseEvent event) {
        kafkaTemplate.send(KafkaConstants.OFFENSE_TOPIC, event);
    }
}
