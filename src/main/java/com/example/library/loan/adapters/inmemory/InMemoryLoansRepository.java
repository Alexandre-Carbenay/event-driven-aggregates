package com.example.library.loan.adapters.inmemory;

import com.example.library.loan.loans.*;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;

@Repository
class InMemoryLoansRepository implements LoansRepository {

    private final Set<Loan> loans = new TreeSet<>();

    @Override
    public Collection<Loan> findByLibraryMemberAndStatus(LibraryId library, MemberId member, LoanStatus status) {
        return internalFindByLibraryAndMember(library, member)
                .filter(loan -> loan.status().equals(status))
                .collect(toList());
    }

    @Override
    public Optional<Loan> findByLibraryMemberAndBook(LibraryId library, MemberId member, BookId book) {
        return internalFindByLibraryAndMember(library, member)
                .filter(loan -> loan.book().equals(book))
                .findFirst();
    }

    @Override
    public Optional<Loan> findByLibraryMemberAndId(LibraryId library, MemberId member, LoanId id) {
        return internalFindByLibraryAndMember(library, member)
                .filter(loan -> loan.id().equals(id))
                .findFirst();
    }

    private Stream<Loan> internalFindByLibraryAndMember(LibraryId library, MemberId member) {
        return loans.stream()
                .filter(loan -> loan.library().equals(library))
                .filter(loan -> loan.member().equals(member));
    }

    @Override
    public Loan save(Loan loan) {
        loans.add(loan);
        return loan;
    }

}
