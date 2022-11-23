package com.example.library.loan.loans;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class InactiveLoanException extends RuntimeException {
    public InactiveLoanException(LibraryId library, MemberId member, LoanId loan) {
        super("Cannot modify inactive loan " + loan + " in library " + library + " for member " + member);
    }
}
