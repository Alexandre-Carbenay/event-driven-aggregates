package com.example.library.loan.adapters.kafka;

import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.kafka.DefaultKafkaProducerFactoryCustomizer;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.boot.context.properties.PropertyMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;
import org.springframework.kafka.core.DefaultKafkaProducerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.core.ProducerFactory;
import org.springframework.kafka.support.ProducerListener;
import org.springframework.kafka.support.converter.RecordMessageConverter;

@Configuration
@RequiredArgsConstructor
class KafkaProducerConfiguration {

    static final String LOANS_TOPIC_NAME = "loans";
    static final String LOANED_BOOKS_TOPIC_NAME = "loaned_books";
    static final String EXTENDED_LOANS_TOPIC_NAME = "extended_loans";
    static final String RETURNED_LOANS_TOPIC_NAME = "returned_loans";

    private final KafkaProperties properties;

    @Bean
    NewTopic loansTopic() {
        return TopicBuilder.name(LOANS_TOPIC_NAME).partitions(1).build();
    }

    @Bean
    NewTopic loanedBooksTopic() {
        return TopicBuilder.name(LOANED_BOOKS_TOPIC_NAME).partitions(1).build();
    }

    @Bean
    NewTopic extendedLoansTopic() {
        return TopicBuilder.name(EXTENDED_LOANS_TOPIC_NAME).partitions(1).build();
    }

    @Bean
    NewTopic returnedLoansTopic() {
        return TopicBuilder.name(RETURNED_LOANS_TOPIC_NAME).partitions(1).build();
    }

    @Bean
    public DefaultKafkaProducerFactory<?, ?> uniqueTopicKafkaProducerFactory(
            ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers) {
        var producerProperties = this.properties.buildProducerProperties();
        producerProperties.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicRecordNameStrategy");
        var factory = new DefaultKafkaProducerFactory<>(producerProperties);
        var transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean
    public KafkaTemplate<?, ?> uniqueTopicKafkaTemplate(@Qualifier("uniqueTopicKafkaProducerFactory") ProducerFactory<Object, Object> kafkaProducerFactory,
                                             ProducerListener<Object, Object> kafkaProducerListener,
                                             ObjectProvider<RecordMessageConverter> messageConverter) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
        map.from(this.properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(this.properties.getTemplate().getTransactionIdPrefix()).to(kafkaTemplate::setTransactionIdPrefix);
        return kafkaTemplate;
    }

    @Bean
    public DefaultKafkaProducerFactory<?, ?> multipleTopicsKafkaProducerFactory(
            ObjectProvider<DefaultKafkaProducerFactoryCustomizer> customizers) {
        var producerProperties = this.properties.buildProducerProperties();
        producerProperties.put("value.subject.name.strategy", "io.confluent.kafka.serializers.subject.TopicNameStrategy");
        var factory = new DefaultKafkaProducerFactory<>(producerProperties);
        var transactionIdPrefix = this.properties.getProducer().getTransactionIdPrefix();
        if (transactionIdPrefix != null) {
            factory.setTransactionIdPrefix(transactionIdPrefix);
        }
        customizers.orderedStream().forEach((customizer) -> customizer.customize(factory));
        return factory;
    }

    @Bean
    public KafkaTemplate<?, ?> multipleTopicsKafkaTemplate(@Qualifier("multipleTopicsKafkaProducerFactory") ProducerFactory<Object, Object> kafkaProducerFactory,
                                                        ProducerListener<Object, Object> kafkaProducerListener,
                                                        ObjectProvider<RecordMessageConverter> messageConverter) {
        PropertyMapper map = PropertyMapper.get().alwaysApplyingWhenNonNull();
        KafkaTemplate<Object, Object> kafkaTemplate = new KafkaTemplate<>(kafkaProducerFactory);
        messageConverter.ifUnique(kafkaTemplate::setMessageConverter);
        map.from(kafkaProducerListener).to(kafkaTemplate::setProducerListener);
        map.from(this.properties.getTemplate().getDefaultTopic()).to(kafkaTemplate::setDefaultTopic);
        map.from(this.properties.getTemplate().getTransactionIdPrefix()).to(kafkaTemplate::setTransactionIdPrefix);
        return kafkaTemplate;
    }

}
