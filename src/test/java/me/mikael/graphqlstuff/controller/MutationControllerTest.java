package me.mikael.graphqlstuff.controller;

import me.mikael.graphqlstuff.entity.Account;
import me.mikael.graphqlstuff.entity.Card;
import me.mikael.graphqlstuff.entity.Customer;
import me.mikael.graphqlstuff.model.AccountType;
import me.mikael.graphqlstuff.model.CardType;
import me.mikael.graphqlstuff.repository.AccountRepository;
import me.mikael.graphqlstuff.repository.CardRepository;
import me.mikael.graphqlstuff.repository.CustomerRepository;
import me.mikael.graphqlstuff.service.CreateCustomerAccountCardService;
import me.mikael.graphqlstuff.service.DeleteCardService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.context.annotation.Import;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ActiveProfiles("test")
@GraphQlTest(MutationController.class)
@Import(CreateCustomerAccountCardService.class)
class MutationControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private CustomerRepository customerRepository;

    @MockitoBean
    private AccountRepository accountRepository;

    @MockitoBean
    private CardRepository cardRepository;

    @MockitoBean
    private DeleteCardService deleteCardService;

    @Test
    void createCustomer_returnsCreatedCustomer() {
        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(inv -> {
                    Customer c = inv.getArgument(0);
                    c.setId(1L);
                    return c;
                });

        graphQlTester.document("""
                        mutation {
                            createCustomer(input: { name: "Alice" }) {
                                id
                                name
                            }
                        }
                        """)
                .execute()
                .path("createCustomer.id").entity(String.class).isEqualTo("1")
                .path("createCustomer.name").entity(String.class).isEqualTo("Alice");
    }

    @Test
    void createAccount_returnsCreatedAccount() {
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(inv -> {
                    Account a = inv.getArgument(0);
                    a.setId(10L);
                    return a;
                });

        graphQlTester.document("""
                        mutation {
                            createAccount(input: { ownerId: 1, accountType: CURRENT, balance: 500.0 }) {
                                id
                                ownerId
                                accountType
                                balance
                            }
                        }
                        """)
                .execute()
                .path("createAccount.id").entity(String.class).isEqualTo("10")
                .path("createAccount.accountType").entity(String.class).isEqualTo("CURRENT");
    }

    @Test
    void createCard_returnsCreatedCard() {
        when(cardRepository.save(any(Card.class)))
                .thenAnswer(inv -> {
                    Card c = inv.getArgument(0);
                    c.setId(100L);
                    return c;
                });

        graphQlTester.document("""
                        mutation {
                            createCard(input: { cardHolderId: 1, accountId: 10, cardType: PHYSICAL }) {
                                id
                                cardHolderId
                                cardType
                                accountId
                            }
                        }
                        """)
                .execute()
                .path("createCard.id").entity(String.class).isEqualTo("100")
                .path("createCard.cardType").entity(String.class).isEqualTo("PHYSICAL");
    }

    @Test
    void createCustomerAccountCard_compositeHappyPath() {
        when(customerRepository.save(any(Customer.class)))
                .thenAnswer(inv -> {
                    Customer c = inv.getArgument(0);
                    c.setId(1L);
                    return c;
                });
        when(accountRepository.save(any(Account.class)))
                .thenAnswer(inv -> {
                    Account a = inv.getArgument(0);
                    a.setId(10L);
                    return a;
                });
        when(cardRepository.save(any(Card.class)))
                .thenAnswer(inv -> {
                    Card c = inv.getArgument(0);
                    c.setId(100L);
                    return c;
                });

        graphQlTester.document("""
                        mutation {
                            createCustomerAccountCard(input: {
                                customer: { name: "Bob" }
                                account: { accountType: CREDIT, balance: 1000.0 }
                                card: { cardType: VIRTUAL }
                            }) {
                                customer { id name }
                                account { id ownerId accountType }
                                card { id cardHolderId accountId }
                                errors { message path }
                            }
                        }
                        """)
                .execute()
                .path("createCustomerAccountCard.customer.name").entity(String.class).isEqualTo("Bob")
                .path("createCustomerAccountCard.account.ownerId").entity(String.class).isEqualTo("1")
                .path("createCustomerAccountCard.card.cardHolderId").entity(String.class).isEqualTo("1")
                .path("createCustomerAccountCard.card.accountId").entity(String.class).isEqualTo("10")
                .path("createCustomerAccountCard.errors").entityList(Object.class).hasSize(0);
    }

    @Test
    void createCustomerAccountCard_cardWithoutCustomerOrAccount_returnsErrors() {
        graphQlTester.document("""
                        mutation {
                            createCustomerAccountCard(input: {
                                card: { cardType: PHYSICAL }
                            }) {
                                customer { id }
                                account { id }
                                card { id }
                                errors { message path }
                            }
                        }
                        """)
                .execute()
                .path("createCustomerAccountCard.errors").entityList(Object.class).hasSizeGreaterThan(0)
                .path("createCustomerAccountCard.card").valueIsNull();
    }
}
