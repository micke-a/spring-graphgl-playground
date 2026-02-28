package me.mikael.graphqlstuff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.graphqlstuff.dto.*;
import me.mikael.graphqlstuff.entity.Account;
import me.mikael.graphqlstuff.entity.Card;
import me.mikael.graphqlstuff.entity.Customer;
import me.mikael.graphqlstuff.repository.AccountRepository;
import me.mikael.graphqlstuff.repository.CardRepository;
import me.mikael.graphqlstuff.repository.CustomerRepository;
import me.mikael.graphqlstuff.service.CreateCustomerAccountCardService;
import me.mikael.graphqlstuff.service.DeleteCardService;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.MutationMapping;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MutationController {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;
    private final CreateCustomerAccountCardService createCustomerAccountCardService;
    private final DeleteCardService deleteCardService;

    @MutationMapping
    public Customer createCustomer(@Argument CreateCustomerInput input) {
        log.info("createCustomer name={}", input.getName());
        Customer customer = new Customer();
        customer.setName(input.getName());
        return customerRepository.save(customer);
    }

    @MutationMapping
    public Account createAccount(@Argument CreateAccountInput input) {
        log.info("createAccount ownerId={}, type={}", input.getOwnerId(), input.getAccountType());
        Account account = new Account();
        account.setOwnerId(input.getOwnerId());
        account.setAccountType(input.getAccountType());
        account.setBalance(input.getBalance());
        return accountRepository.save(account);
    }

    @MutationMapping
    public Card createCard(@Argument CreateCardInput input) {
        log.info("createCard cardHolderId={}, accountId={}", input.getCardHolderId(), input.getAccountId());
        Card card = new Card();
        card.setCardHolderId(input.getCardHolderId());
        card.setAccountId(input.getAccountId());
        card.setCardType(input.getCardType());
        return cardRepository.save(card);
    }

    @MutationMapping
    public CreateCustomerAccountCardPayload createCustomerAccountCard(@Argument CreateCustomerAccountCardInput input) {
        log.info("createCustomerAccountCard");
        return createCustomerAccountCardService.create(input);
    }

    @MutationMapping
    public DeleteCardPayload deleteCard(@Argument Long cardId) {
        log.info("deleteCard cardId={}", cardId);
        return deleteCardService.delete(cardId);
    }
}
