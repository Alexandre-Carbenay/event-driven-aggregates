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

import static com.example.library.reporting.ReportingService.UNIQUE;

@Slf4j
@Component
@KafkaListener(groupId = "reporting", topics = "loans")
@RequiredArgsConstructor
public class UniqueReportingKafkaConsumer {

    private final ReportingService service;

    @KafkaHandler
    public void uniqueBookLoaned(@Payload BookLoaned event) {
        log.info("[Unique] Book loaned: {}", event);
        service.bookLoaned(UNIQUE, event.getLoanId());
    }

    @KafkaHandler
    public void uniqueBookLoanExtended(@Payload BookLoanExtended event) {
        log.info("[Unique] Book loan extended: {}", event);
        service.bookLoanExtended(UNIQUE, event.getLoanId());
    }

    @KafkaHandler
    public void uniqueBookReturned(@Payload BookReturned event) {
        log.info("[Unique] Book returned: {}", event);
        service.bookReturned(UNIQUE, event.getLoanId());
    }

}
