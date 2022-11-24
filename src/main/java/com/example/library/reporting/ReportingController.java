package com.example.library.reporting;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/reporting", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class ReportingController {

    private final ReportingService service;

    @GetMapping
    public ReportingResource getReporting() {
        var reporting = service.getReporting();
        return new ReportingResource(
                reporting.totalLoans(),
                reporting.booksLoaned(),
                reporting.loansExtended(),
                reporting.missedLoansExtended(),
                reporting.booksReturned(),
                reporting.missedBooksReturned()
        );
    }

    private record ReportingResource(
            int totalLoans,
            int booksLoaned,
            int loansExtended,
            int missedLoansExtended,
            int booksReturned,
            int missedBooksReturned
    ) {

    }

}
