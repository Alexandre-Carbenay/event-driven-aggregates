package com.example.library.reporting;

import com.example.library.loan.BookLoanExtended;
import com.example.library.loan.BookLoaned;
import com.example.library.loan.BookReturned;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@KafkaListener(groupId = "reporting", topics = "loans")
@RequiredArgsConstructor
public class ReportingKafkaConsumer {

    private final ReportingService service;

    @KafkaHandler
    public void bookLoaned(@Payload BookLoaned event) {
        log.info("Book loaned: {}", event);
        service.bookLoaned(event.getLoanId());
    }

    @KafkaHandler
    public void bookLoanExtended(@Payload BookLoanExtended event) {
        log.info("Book loan extended: {}", event);
        service.bookLoanExtended(event.getLoanId());
    }

    @KafkaHandler
    public void bookReturned(@Payload BookReturned event) {
        log.info("Book returned: {}", event);
        service.bookReturned(event.getLoanId());
    }

}
