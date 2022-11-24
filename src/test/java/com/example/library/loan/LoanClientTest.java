package com.example.library.loan;

import com.example.library.loan.adapters.rest.LoanResource;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import net.jqwik.api.Arbitraries;
import net.jqwik.api.Combinators;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.junit.jupiter.api.Test;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import java.io.IOException;
import java.io.UncheckedIOException;

import static net.jqwik.api.Arbitraries.strings;

@Slf4j
class LoanClientTest {

    private final OkHttpClient client = new OkHttpClient();
    private final ObjectMapper mapper = new Jackson2ObjectMapperBuilder()
            .featuresToDisable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES)
            .build();

    @Test
    void interactWithLoans() {
        var libraryId = strings().alpha().ofMinLength(5).ofMaxLength(10).sample();
        var memberIds = strings().alpha().ofMinLength(5).ofMaxLength(10)
                .list().uniqueElements().ofSize(5).sample();
        Combinators.combine(
                        Arbitraries.of(memberIds),
                        strings().alpha().numeric().ofMinLength(5).ofMaxLength(15)
                ).as(Loan::new)
                .sampleStream()
                .distinct()
                .limit(100)
                .forEach(loan -> processLoan(libraryId, loan));
    }

    void processLoan(String library, Loan loan) {
        var loanId = loanBook(library, loan);
        extendLoan(library, loan.member, loanId);
        returnLoan(library, loan.member, loanId);
    }

    String loanBook(String library, Loan loan) {
        log.info("Loan book {} for member {} in library {}", loan.book, loan.member, library);
        var url = String.format("http://localhost:8080/loans/%s/%s/%s", library, loan.member, loan.book);
        try {
            var response = postWithoutBody(url);
            LoanResource resource = mapper.readValue(response.body().string(), LoanResource.class);
            response.close();
            return resource.id();
        } catch (IOException e) {
            log.error("Error while loaning book {} for member {} in library {}", loan.book, loan.member, library, e);
            throw new UncheckedIOException(e);
        }
    }

    void extendLoan(String library, String member, String loanId) {
        log.info("Extend loan {} for member {} in library {}", loanId, member, library);
        var url = String.format("http://localhost:8080/loans/%s/%s/%s/extension", library, member, loanId);
        try {
            postWithoutBody(url).close();
        } catch (IOException e) {
            log.error("Error while extending loan {} for member {} in library {}", loanId, member, library, e);
            throw new UncheckedIOException(e);
        }
    }

    void returnLoan(String library, String member, String loanId) {
        log.info("Return loan {} for member {} in library {}", loanId, member, library);
        var url = String.format("http://localhost:8080/loans/%s/%s/%s/return", library, member, loanId);
        try {
            postWithoutBody(url).close();
        } catch (IOException e) {
            log.error("Error while returning loan {} for member {} in library {}", loanId, member, library, e);
            throw new UncheckedIOException(e);
        }
    }

    Response postWithoutBody(String url) throws IOException {
        var request = new Request.Builder()
                .url(url)
                .post(RequestBody.create(new byte[0]))
                .build();

        return client.newCall(request).execute();
    }

    record Loan(String member, String book) {
    }

}
