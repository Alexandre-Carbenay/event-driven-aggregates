package com.example.library.loan.loans;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.experimental.Accessors;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Comparator;

import static com.example.library.loan.loans.LoanStatus.*;

@Data
@Accessors(fluent = true)
@RequiredArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Loan implements Comparable<Loan> {

    private static final Comparator<Loan> CREATED_AT_COMPARATOR = Comparator.comparing(Loan::createdAt);

    @NonNull
    @EqualsAndHashCode.Include
    private final LoanId id;
    @NonNull
    private LoanStatus status;
    @NonNull
    private final LibraryId library;
    @NonNull
    private final MemberId member;
    @NonNull
    private final BookId book;
    @NonNull
    private final LocalDate startAt;
    private LocalDate returnedAt;
    @NonNull
    private LocalDate returnBefore;
    @NonNull
    private final LocalDateTime createdAt;
    @NonNull
    private LocalDateTime updatedAt;

    public static Loan from(LoanBook loanBook) {
        var createdAt = LocalDateTime.now();
        var startAt = createdAt.toLocalDate();
        var returnBefore = calculateExpectedReturnDate(startAt);
        return new Loan(
                LoanId.generate(),
                ACTIVE,
                loanBook.library(),
                loanBook.member(),
                loanBook.book(),
                startAt,
                returnBefore,
                createdAt,
                createdAt
        );
    }

    private static LocalDate calculateExpectedReturnDate(LocalDate startAt) {
        return startAt.plusWeeks(3);
    }

    public boolean isActive() {
        return ACTIVE.equals(status);
    }

    public void returnIt() {
        if (!isActive()) {
            throw new InactiveLoanException(library, member, id);
        }
        updatedAt = LocalDateTime.now();
        returnedAt = updatedAt.toLocalDate();
        status = RETURNED;
    }

    public void extend() {
        if (!isActive()) {
            throw new InactiveLoanException(library, member, id);
        }
        returnBefore = calculateExpectedReturnDate(returnBefore);
        updatedAt = LocalDateTime.now();
    }

    public void cancel() {
        if (!isActive()) {
            throw new InactiveLoanException(library, member, id);
        }
        status = CANCELED;
    }

    @Override
    public int compareTo(@NonNull Loan loan) {
        return CREATED_AT_COMPARATOR.compare(this, loan);
    }
}
