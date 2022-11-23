package com.example.library.loan.loans;

import java.util.UUID;

public record LoanId(UUID value) {

    public static LoanId of(UUID value) {
        return new LoanId(value);
    }

    public static LoanId generate() {
        return new LoanId(UUID.randomUUID());
    }

}
