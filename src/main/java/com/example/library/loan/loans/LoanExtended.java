package com.example.library.loan.loans;

import com.example.library.loan.events.Event;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record LoanExtended(
        LocalDateTime occurredAt,
        LoanId loan,
        LibraryId library,
        MemberId member,
        BookId book,
        LocalDate startAt,
        LocalDate originalReturnBefore,
        LocalDate currentReturnBefore
) implements Event {

    public static final String TYPE = "LOAN_EXTENDED";

    static LoanExtended from(Loan loan, LocalDate originalReturnBefore) {
        return new LoanExtended(
                loan.updatedAt(),
                loan.id(),
                loan.library(),
                loan.member(),
                loan.book(),
                loan.startAt(),
                originalReturnBefore,
                loan.returnBefore()
        );
    }

    @Override
    public String type() {
        return TYPE;
    }

}
