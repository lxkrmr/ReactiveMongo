package com.example.reactivemongo;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface AccountCrudRepository extends ReactiveCrudRepository<Account, String> {

    Flux<Account> findAllByValue(Double value);

    Mono<Account> findFirstByOwner(String owner);

}
