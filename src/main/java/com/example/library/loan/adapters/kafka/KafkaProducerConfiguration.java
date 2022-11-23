package com.example.library.loan.adapters.kafka;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

@Configuration
class KafkaProducerConfiguration {

    static final String LOANS_TOPIC_NAME = "loans";

    @Bean
    NewTopic loanTopic() {
        return TopicBuilder.name(LOANS_TOPIC_NAME).partitions(1).build();
    }

}
