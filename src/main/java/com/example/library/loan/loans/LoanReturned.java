package com.example.library.loan.loans;

import com.example.library.loan.events.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanReturned(
        LocalDateTime occurredAt,
        LoanId loan,
        LibraryId library,
        MemberId member,
        BookId book,
        LocalDate startAt,
        LocalDate returnAt,
        LocalDate originalReturnBefore
) implements Event {

    public static final String TYPE = "LOAN_RETURNED";

    static LoanReturned from(Loan loan) {
        return new LoanReturned(
                loan.updatedAt(),
                loan.id(),
                loan.library(),
                loan.member(),
                loan.book(),
                loan.startAt(),
                loan.returnedAt().orElseThrow(),
                loan.returnBefore()
        );
    }

    @Override
    public String type() {
        return TYPE;
    }

}
