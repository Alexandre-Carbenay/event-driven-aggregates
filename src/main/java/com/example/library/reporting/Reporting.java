package com.example.library.reporting;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import static com.example.library.reporting.Reporting.ReportStatus.*;

public class Reporting {

    private final Map<String, ReportStatus> loans = new HashMap<>();
    private final AtomicInteger missedLoanExtended = new AtomicInteger(0);
    private final AtomicInteger missedBookReturned = new AtomicInteger(0);

    public int totalLoans() {
        return loans.size();
    }

    public boolean hasLoan(String loanId) {
        return loans.containsKey(loanId);
    }

    public void loanedBook(String loanId) {
        loans.put(loanId, LOANED);
    }

    public int booksLoaned() {
        return loansWithStatus(LOANED);
    }

    public void extendedLoan(String loanId) {
        loans.put(loanId, EXTENDED);
    }

    public void reportMissedLoanExtended() {
        missedLoanExtended.incrementAndGet();
    }

    public int loansExtended() {
        return loansWithStatus(EXTENDED);
    }

    public int missedLoansExtended() {
        return missedLoanExtended.get();
    }

    public void reportMissedBookReturned() {
        missedBookReturned.incrementAndGet();
    }

    public void returnedBook(String loanId) {
        loans.put(loanId, RETURNED);
    }

    public int booksReturned() {
        return loansWithStatus(RETURNED);
    }

    public int missedBooksReturned() {
        return missedBookReturned.get();
    }

    private int loansWithStatus(ReportStatus status) {
        return (int) loans.keySet().stream()
                .filter(loanId -> loans.get(loanId).equals(status))
                .count();
    }

    public enum ReportStatus {
        LOANED,
        EXTENDED,
        RETURNED;
    }

}
