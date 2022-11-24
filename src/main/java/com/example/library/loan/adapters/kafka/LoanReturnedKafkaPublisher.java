package com.example.library.loan.adapters.kafka;

import com.example.library.loan.BookReturned;
import com.example.library.loan.events.EventHandler;
import com.example.library.loan.loans.LoanReturned;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static com.example.library.loan.adapters.kafka.KafkaProducerConfiguration.LOANS_TOPIC_NAME;
import static com.example.library.loan.adapters.kafka.KafkaProducerConfiguration.RETURNED_LOANS_TOPIC_NAME;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Component
public class LoanReturnedKafkaPublisher implements EventHandler<LoanReturned> {

    private final KafkaTemplate<String, BookReturned> uniqueTopicKafkaTemplate;
    private final KafkaTemplate<String, BookReturned> multipleTopicsKafkaTemplate;

    public LoanReturnedKafkaPublisher(
            @Qualifier("uniqueTopicKafkaTemplate") KafkaTemplate<String, BookReturned> uniqueTopicKafkaTemplate,
            @Qualifier("multipleTopicsKafkaTemplate") KafkaTemplate<String, BookReturned> multipleTopicsKafkaTemplate) {
        this.uniqueTopicKafkaTemplate = uniqueTopicKafkaTemplate;
        this.multipleTopicsKafkaTemplate = multipleTopicsKafkaTemplate;
    }

    @Override
    public boolean canHandle(String type) {
        return LoanReturned.TYPE.equals(type);
    }

    @Override
    public void handle(LoanReturned event) {
        var eventToPublish = new BookReturned(
                event.occurredAt().format(ISO_DATE_TIME),
                event.loan().value().toString(),
                event.library().value(),
                event.book().value(),
                event.member().value(),
                event.startAt().format(ISO_DATE),
                event.originalReturnBefore().format(ISO_DATE),
                event.returnAt().format(ISO_DATE)
        );
        produceToUniqueTopic(eventToPublish);
        produceToMultipleTopic(eventToPublish);
    }

    private void produceToUniqueTopic(BookReturned eventToPublish) {
        List<Header> headers = List.of(
                new RecordHeader("type", "BookReturned".getBytes())
        );
        var eventRecord = new ProducerRecord<>(
                LOANS_TOPIC_NAME,
                null,
                Instant.now().toEpochMilli(),
                eventToPublish.getLoanId(),
                eventToPublish,
                headers
        );
        uniqueTopicKafkaTemplate.send(eventRecord);
    }

    private void produceToMultipleTopic(BookReturned eventToPublish) {
        var eventRecord = new ProducerRecord<>(
                RETURNED_LOANS_TOPIC_NAME,
                null,
                Instant.now().toEpochMilli(),
                eventToPublish.getLoanId(),
                eventToPublish
        );
        multipleTopicsKafkaTemplate.send(eventRecord);
    }

}
