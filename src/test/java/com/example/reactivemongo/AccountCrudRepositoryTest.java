package com.example.reactivemongo;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataMongoTest
class AccountCrudRepositoryTest {

    @AfterEach
    void clean() {
        repository.deleteAll().block();
    }

    @Autowired
    private AccountCrudRepository repository;

    @Test
    void givenTwoAccountsWithSameOwner_whenFindFirstByOwner_thenFindFirstAccount() {
        // given
        Account firstAccount = new Account(null, "Foo", 42d);
        Account secondAccount = new Account(null, "Foo", 12d);
        repository.save(firstAccount).block();
        repository.save(secondAccount).block();

        // when
        Mono<Account> result = repository.findFirstByOwner("Foo");

        // then
        StepVerifier.create(result)
                    .assertNext(found -> assertThat(found.value()).isEqualTo(firstAccount.value()))
                    .verifyComplete();
    }

    @Test
    void givenNoAccountForOwner_whenFindFirstByOwner_thenFoundNothing() {
        // when
        Mono<Account> result = repository.findFirstByOwner("Foo");

        // then
        StepVerifier.create(result)
                    .expectNextCount(0)
                    .verifyComplete();
    }

    @Test
    void givenTwoAccountsWithSameValue_whenFindAllByValue_thenFindAllAccounts() {
        // given
        Account firstAccount = new Account(null, "Foo", 42d);
        Account secondAccount = new Account(null, "Bar", 42d);
        repository.save(firstAccount).block();
        repository.save(secondAccount).block();

        // when
        Flux<Account> result = repository.findAllByValue(42d);

        // then
        StepVerifier.create(result)
                    .assertNext(foundFirst -> assertThat(foundFirst.owner()).isEqualTo(firstAccount.owner()))
                    .assertNext(foundSecond -> assertThat(foundSecond.owner()).isEqualTo(secondAccount.owner()))
                    .verifyComplete();
    }

    @Test
    void givenNoAccountWithValue_whenFindAllByValue_thenFoundNothing() {
        // when
        Flux<Account> result = repository.findAllByValue(42d);

        // then
        StepVerifier.create(result)
                    .expectNextCount(0)
                    .verifyComplete();
    }
}
