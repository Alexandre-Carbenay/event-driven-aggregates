package com.example.library.loan.loans;

public record MemberId(String value) {

    public static MemberId of(String value) {
        return new MemberId(value);
    }

}
