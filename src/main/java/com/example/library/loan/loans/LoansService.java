package com.example.library.loan.loans;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collection;

import static com.example.library.loan.loans.LoanStatus.ACTIVE;

@Service
@RequiredArgsConstructor
public class LoansService {

    private final LoansRepository repository;

    public Collection<Loan> getActiveLoans(LoansQuery query) {
        return repository.findByLibraryMemberAndStatus(query.library, query.member, ACTIVE);
    }

    public Loan loanBook(LoanBook command) {
        var existingLoan = repository.findByLibraryMemberAndBook(command.library(), command.member(), command.book());
        if (existingLoan.isPresent() && existingLoan.get().isActive()) {
            throw new BookAlreadyLoanedException(command.library(), command.member(), command.book());
        }
        var loan = Loan.from(command);
        return repository.save(loan);
    }

    public Loan returnLoan(ReturnLoan command) {
        var loan = repository.findByLibraryMemberAndId(command.library(), command.member(), command.loan())
                .orElseThrow(() -> new UnknownLoanException(command.library(), command.member(), command.loan()));
        loan.returnIt();
        return repository.save(loan);
    }

    public Loan extendLoan(ExtendLoan command) {
        var loan = repository.findByLibraryMemberAndId(command.library(), command.member(), command.loan())
                .orElseThrow(() -> new UnknownLoanException(command.library(), command.member(), command.loan()));
        loan.extend();
        return repository.save(loan);
    }

    public void cancelLoan(CancelLoan command) {
        var loan = repository.findByLibraryMemberAndId(command.library(), command.member(), command.loan())
                .orElseThrow(() -> new UnknownLoanException(command.library(), command.member(), command.loan()));
        loan.cancel();
        repository.save(loan);
    }

    public record LoansQuery(LibraryId library, MemberId member) {
    }

}
