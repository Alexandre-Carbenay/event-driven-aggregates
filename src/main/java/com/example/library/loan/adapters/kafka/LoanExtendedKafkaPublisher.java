package com.example.library.loan.adapters.kafka;

import com.example.library.loan.BookLoanExtended;
import com.example.library.loan.BookLoaned;
import com.example.library.loan.events.EventHandler;
import com.example.library.loan.loans.LoanExtended;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static com.example.library.loan.adapters.kafka.KafkaProducerConfiguration.EXTENDED_LOANS_TOPIC_NAME;
import static com.example.library.loan.adapters.kafka.KafkaProducerConfiguration.LOANS_TOPIC_NAME;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Component
public class LoanExtendedKafkaPublisher implements EventHandler<LoanExtended> {

    private final KafkaTemplate<String, BookLoanExtended> uniqueTopicKafkaTemplate;
    private final KafkaTemplate<String, BookLoanExtended> multipleTopicsKafkaTemplate;

    public LoanExtendedKafkaPublisher(
            @Qualifier("uniqueTopicKafkaTemplate") KafkaTemplate<String, BookLoanExtended> uniqueTopicKafkaTemplate,
            @Qualifier("multipleTopicsKafkaTemplate") KafkaTemplate<String, BookLoanExtended> multipleTopicsKafkaTemplate) {
        this.uniqueTopicKafkaTemplate = uniqueTopicKafkaTemplate;
        this.multipleTopicsKafkaTemplate = multipleTopicsKafkaTemplate;
    }

    @Override
    public boolean canHandle(String type) {
        return LoanExtended.TYPE.equals(type);
    }

    @Override
    public void handle(LoanExtended event) {
        var eventToPublish = new BookLoanExtended(
                event.occurredAt().format(ISO_DATE_TIME),
                event.loan().value().toString(),
                event.library().value(),
                event.book().value(),
                event.member().value(),
                event.startAt().format(ISO_DATE),
                event.originalReturnBefore().format(ISO_DATE),
                event.currentReturnBefore().format(ISO_DATE)
        );
        List<Header> headers = List.of(
                new RecordHeader("type", "BookLoanExtended".getBytes())
        );
        produceToUniqueTopic(eventToPublish, headers);
        produceToMultipleTopic(eventToPublish, headers);
    }

    private void produceToUniqueTopic(BookLoanExtended eventToPublish, List<Header> headers) {
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

    private void produceToMultipleTopic(BookLoanExtended eventToPublish, List<Header> headers) {
        var eventRecord = new ProducerRecord<>(
                EXTENDED_LOANS_TOPIC_NAME,
                null,
                Instant.now().toEpochMilli(),
                eventToPublish.getLoanId(),
                eventToPublish,
                headers
        );
        multipleTopicsKafkaTemplate.send(eventRecord);
    }

}
