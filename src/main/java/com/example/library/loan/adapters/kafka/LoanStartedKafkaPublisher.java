package com.example.library.loan.adapters.kafka;

import com.example.library.loan.BookLoaned;
import com.example.library.loan.events.EventHandler;
import com.example.library.loan.loans.LoanStarted;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.Header;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.List;

import static com.example.library.loan.adapters.kafka.KafkaProducerConfiguration.LOANS_TOPIC_NAME;
import static java.time.format.DateTimeFormatter.ISO_DATE;
import static java.time.format.DateTimeFormatter.ISO_DATE_TIME;

@Component
@RequiredArgsConstructor
public class LoanStartedKafkaPublisher implements EventHandler<LoanStarted> {

    private final KafkaTemplate<String, BookLoaned> kafkaTemplate;

    @Override
    public boolean canHandle(String type) {
        return LoanStarted.TYPE.equals(type);
    }

    @Override
    public void handle(LoanStarted event) {
        var eventToPublish = new BookLoaned(
                event.occurredAt().format(ISO_DATE_TIME),
                event.loan().value().toString(),
                event.library().value(),
                event.book().value(),
                event.member().value(),
                event.startAt().format(ISO_DATE),
                event.returnBefore().format(ISO_DATE)
        );
        List<Header> headers = List.of(
                new RecordHeader("type", "BookLoaned".getBytes())
        );
        var eventRecord = new ProducerRecord<>(
                LOANS_TOPIC_NAME,
                null,
                Instant.now().toEpochMilli(),
                eventToPublish.getLoanId(),
                eventToPublish,
                headers
        );
        kafkaTemplate.send(eventRecord);
    }

}
