package com.example.library.loan.loans;

public record BookId(String value) {

    public static BookId of(String value) {
        return new BookId(value);
    }

}
