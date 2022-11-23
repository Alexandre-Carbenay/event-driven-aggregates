package com.example.library.loan.loans;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.FORBIDDEN)
public class BookAlreadyLoanedException extends RuntimeException {
    public BookAlreadyLoanedException(LibraryId library, MemberId member, BookId book) {
        super("Book " + book + " is already loaned by member " + member + " in library " + library);
    }
}
