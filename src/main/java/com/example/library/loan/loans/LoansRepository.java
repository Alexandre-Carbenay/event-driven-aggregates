package com.example.library.loan.loans;

import java.util.Collection;
import java.util.Optional;

public interface LoansRepository {

    Collection<Loan> findByLibraryMemberAndStatus(LibraryId library, MemberId member, LoanStatus status);

    Optional<Loan> findByLibraryMemberAndBook(LibraryId library, MemberId member, BookId book);

    Optional<Loan> findByLibraryMemberAndId(LibraryId library, MemberId member, LoanId loan);

    Loan save(Loan loan);

}
