package me.mikael.graphqlstuff.controller;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.graphqlstuff.entity.Account;
import me.mikael.graphqlstuff.repository.AccountRepository;
import me.mikael.graphqlstuff.repository.CardRepository;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.execution.BatchLoaderRegistry;
import org.springframework.stereotype.Controller;
import reactor.core.publisher.Flux;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class AccountController {
    private final AccountRepository accountRepository;
    private final BatchLoaderRegistry registry;

    @PostConstruct
    public void init() {
        log.info("init - registering batch loaders");
        registry.forTypePair(Long.class, Account.class)
                .registerBatchLoader(
                        (accountIds, env) -> Flux.fromIterable(accountRepository.findAllById(accountIds))
                );
    }

    @EntityMapping
    public Account account(@Argument Long id){
        log.info("entityMapping account id={}", id);
        return accountRepository.findById(id).orElse(null);
    }
    @EntityMapping
    public List<Account> card(@Argument List<Long> ids){
        log.info("entityMapping accounts ids={}", ids);
        return accountRepository.findAllById(ids);
    }

//    @BatchMapping
//    public Map<Card, Account> cards(@Argument List<Account> accounts){
//        log.info("batchMapping accounts={}", accounts);
//        return accounts.stream()
//                .collect(java.util.stream.Collectors.toMap(
//                        account -> account,
//                        account -> accountRepository.findById(account.g).orElse(null))
//                );
//    }

    @QueryMapping
    public List<Account> getAllAccounts(){
        log.info("getAllAccounts");
        return accountRepository.findAll();
    }

    @QueryMapping
    public Account getAccount(@Argument Long id, @Argument String owner){
        log.info("getAccount id={}, owner={}", id, owner);
        if(id != null){
            return accountRepository.findById(id).orElse(null);
        }
        if(owner != null){
            return accountRepository.findByOwner(owner).stream().findFirst().orElse(null);
        }
        return null;
    }
}
