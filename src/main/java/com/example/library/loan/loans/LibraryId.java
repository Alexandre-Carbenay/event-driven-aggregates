package com.example.library.loan.loans;

public record LibraryId(String value) {

    public static LibraryId of(String value) {
        return new LibraryId(value);
    }

}
