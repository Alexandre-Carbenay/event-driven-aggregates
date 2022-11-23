package com.example.library.loan.loans;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class UnknownLoanException extends RuntimeException {
    public UnknownLoanException(LibraryId library, MemberId member, LoanId loan) {
        super("Cannot find loan with id " + loan + " in library " + library + " for member " + member);
    }
}
