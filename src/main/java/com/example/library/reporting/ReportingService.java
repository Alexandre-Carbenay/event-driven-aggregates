package com.example.library.reporting;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaHandler;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ReportingService {

    @Getter
    private final Reporting reporting = new Reporting();

    public void bookLoaned(String loanId) {
        simulateLongTreatment();
        reporting.loanedBook(loanId);
    }

    @SneakyThrows
    private void simulateLongTreatment() {
        Thread.sleep(200);
    }

    @KafkaHandler
    public void bookLoanExtended(String loanId) {
        if (reporting.hasLoan(loanId)) {
            reporting.extendedLoan(loanId);
        } else {
            log.warn("No loan found with id {}", loanId);
            reporting.reportMissedLoanExtended();
        }
    }

    @KafkaHandler
    public void bookReturned(String loanId) {
        if (reporting.hasLoan(loanId)) {
            reporting.returnedBook(loanId);
        } else {
            log.warn("No loan found with id {}", loanId);
            reporting.reportMissedBookReturned();
        }
    }

}
