package com.example.library.loan.adapters.rest;

import com.example.library.loan.loans.*;
import com.example.library.loan.loans.LoansService.LoansQuery;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.UUID;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping(value = "/loans", produces = APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
class LoansController {

    private final LoansService service;

    @GetMapping("/{library}/{member}")
    public Collection<LoanResource> getLoans(@PathVariable String library, @PathVariable String member) {
        var loans = service.getActiveLoans(new LoansQuery(LibraryId.of(library), MemberId.of(member)));
        return loans.stream().map(LoanResource::from).toList();
    }

    @PostMapping("/{library}/{member}/{book}")
    public LoanResource loanBook(@PathVariable String library, @PathVariable String member, @PathVariable String book) {
        var loan = service.loanBook(new LoanBook(LibraryId.of(library), MemberId.of(member), BookId.of(book)));
        return LoanResource.from(loan);
    }

    @PostMapping("/{library}/{member}/{loan}/return")
    public LoanResource returnLoan(@PathVariable String library, @PathVariable String member, @PathVariable String loan) {
        var returnedLoan = service.returnLoan(new ReturnLoan(LibraryId.of(library), MemberId.of(member), LoanId.of(UUID.fromString(loan))));
        return LoanResource.from(returnedLoan);
    }

    @PostMapping("/{library}/{member}/{loan}/extension")
    public LoanResource extendLoan(@PathVariable String library, @PathVariable String member, @PathVariable String loan) {
        var extendedLoan = service.extendLoan(new ExtendLoan(LibraryId.of(library), MemberId.of(member), LoanId.of(UUID.fromString(loan))));
        return LoanResource.from(extendedLoan);
    }

}
