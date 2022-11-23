package com.example.library.loan.loans;

import com.example.library.loan.events.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanStarted(
        LocalDateTime occurredAt,
        LoanId loan,
        LibraryId library,
        MemberId member,
        BookId book,
        LocalDate startAt,
        LocalDate returnBefore
) implements Event {

    public static final String TYPE = "LOAN_STARTED";

    static LoanStarted from(Loan loan) {
        return new LoanStarted(
                loan.createdAt(),
                loan.id(),
                loan.library(),
                loan.member(),
                loan.book(),
                loan.startAt(),
                loan.returnBefore()
        );
    }

    @Override
    public String type() {
        return TYPE;
    }

}
