package me.mikael.graphqlstuff.service;

import lombok.RequiredArgsConstructor;
import me.mikael.graphqlstuff.dto.*;
import me.mikael.graphqlstuff.entity.Account;
import me.mikael.graphqlstuff.entity.Card;
import me.mikael.graphqlstuff.entity.Customer;
import me.mikael.graphqlstuff.repository.AccountRepository;
import me.mikael.graphqlstuff.repository.CardRepository;
import me.mikael.graphqlstuff.repository.CustomerRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CreateCustomerAccountCardService {

    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final CardRepository cardRepository;

    @Transactional
    public CreateCustomerAccountCardPayload create(CreateCustomerAccountCardInput input) {
        List<UserError> errors = new ArrayList<>();
        Customer customer = null;
        Account account = null;
        Card card = null;

        // Validate: card requires both a customer and an account
        if (input.getCard() != null && input.getCustomer() == null && input.getCard().getCardHolderId() == null) {
            errors.add(new UserError("Card requires a customer (provide customer input or cardHolderId)", List.of("input", "card", "cardHolderId")));
        }
        if (input.getCard() != null && input.getAccount() == null && input.getCard().getAccountId() == null) {
            errors.add(new UserError("Card requires an account (provide account input or accountId)", List.of("input", "card", "accountId")));
        }

        // Validate: account requires a customer
        if (input.getAccount() != null && input.getCustomer() == null && input.getAccount().getOwnerId() == null) {
            errors.add(new UserError("Account requires a customer (provide customer input or ownerId)", List.of("input", "account", "ownerId")));
        }

        if (!errors.isEmpty()) {
            return new CreateCustomerAccountCardPayload(null, null, null, errors);
        }

        // Create in dependency order: Customer -> Account -> Card
        if (input.getCustomer() != null) {
            customer = new Customer();
            customer.setName(input.getCustomer().getName());
            customer = customerRepository.save(customer);
        }

        if (input.getAccount() != null) {
            account = new Account();
            account.setOwnerId(input.getAccount().getOwnerId() != null
                    ? input.getAccount().getOwnerId()
                    : customer.getId());
            account.setAccountType(input.getAccount().getAccountType());
            account.setBalance(input.getAccount().getBalance());
            account = accountRepository.save(account);
        }

        if (input.getCard() != null) {
            card = new Card();
            card.setCardHolderId(input.getCard().getCardHolderId() != null
                    ? input.getCard().getCardHolderId()
                    : customer.getId());
            card.setAccountId(input.getCard().getAccountId() != null
                    ? input.getCard().getAccountId()
                    : account.getId());
            card.setCardType(input.getCard().getCardType());
            card = cardRepository.save(card);
        }

        return new CreateCustomerAccountCardPayload(customer, account, card, errors);
    }
}
