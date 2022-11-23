package com.example.library.loan.adapters.rest;

import com.example.library.loan.loans.Loan;

import java.time.LocalDate;
import java.time.LocalDateTime;

record LoanResource(
        String id,
        String library,
        String member,
        String book,
        LocalDate startAt,
        LocalDate returnBefore,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {

    static LoanResource from(Loan loan) {
        return new LoanResource(
                loan.id().value().toString(),
                loan.library().value(),
                loan.member().value(),
                loan.book().value(),
                loan.startAt(),
                loan.returnBefore(),
                loan.createdAt(),
                loan.updatedAt()
        );
    }

}
