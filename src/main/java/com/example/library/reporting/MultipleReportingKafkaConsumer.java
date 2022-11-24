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

import static com.example.library.reporting.ReportingService.MULTIPLE;

@Slf4j
@Component
@RequiredArgsConstructor
public class MultipleReportingKafkaConsumer {

    private final ReportingService service;

    @KafkaListener(groupId = "reporting", topics = "loaned_books")
    public void multipleBookLoaned(@Payload BookLoaned event) {
        log.info("[Multiple] Book loaned: {}", event);
        service.bookLoaned(MULTIPLE, event.getLoanId());
    }

    @KafkaListener(groupId = "reporting", topics = "extended_loans")
    public void multipleBookLoanExtended(@Payload BookLoanExtended event) {
        log.info("[Multiple] Book loan extended: {}", event);
        service.bookLoanExtended(MULTIPLE, event.getLoanId());
    }

    @KafkaListener(groupId = "reporting", topics = "returned_loans")
    public void multipleBookReturned(@Payload BookReturned event) {
        log.info("[Multiple] Book returned: {}", event);
        service.bookReturned(MULTIPLE, event.getLoanId());
    }

}
