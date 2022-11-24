package com.example.library.reporting;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Random;

@Slf4j
@Service
public class ReportingService {

    public static final String UNIQUE = "unique";
    public static final String MULTIPLE = "multiple";

    private final Random random = new Random();

    @Getter
    private final List<Reporting> reportings = List.of(
            new Reporting(UNIQUE),
            new Reporting(MULTIPLE));

    public Reporting getReporting(String name) {
        return reportings.stream()
                .filter(reporting -> reporting.name().equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No reporting with name " + name));
    }

    public void bookLoaned(String reportName, String loanId) {
        simulateLongTreatment();
        getReporting(reportName).loanedBook(loanId);
    }

    @SneakyThrows
    private void simulateLongTreatment() {
        Thread.sleep(random.nextInt(10));
    }

    public void bookLoanExtended(String reportName, String loanId) {
        var reporting = getReporting(reportName);
        if (reporting.hasLoan(loanId)) {
            reporting.extendedLoan(loanId);
        } else {
            log.warn("No loan found with id {}", loanId);
            reporting.reportMissedLoanExtended();
        }
    }

    public void bookReturned(String reportName, String loanId) {
        var reporting = getReporting(reportName);
        if (reporting.hasLoan(loanId)) {
            reporting.returnedBook(loanId);
        } else {
            log.warn("No loan found with id {}", loanId);
            reporting.reportMissedBookReturned();
        }
    }

}
