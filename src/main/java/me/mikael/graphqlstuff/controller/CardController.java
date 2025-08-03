package me.mikael.graphqlstuff.controller;

import graphql.GraphQLContext;
import graphql.schema.DataFetchingEnvironment;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.graphqlstuff.entity.Account;
import me.mikael.graphqlstuff.entity.Card;
import me.mikael.graphqlstuff.repository.AccountRepository;
import me.mikael.graphqlstuff.repository.CardRepository;
import org.springframework.graphql.data.federation.EntityMapping;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.BatchMapping;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.graphql.data.method.annotation.SchemaMapping;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Map;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CardController {

    private final CardRepository cardRepository;
    private final AccountRepository accountRepository;

    @EntityMapping
    public Card card(@Argument Long id){
        log.info("entityMapping card id={}", id);
        return cardRepository.findById(id).orElse(null);
    }
    @EntityMapping
    public List<Card> card(@Argument List<Long> ids){
        log.info("entityMapping cards ids={}", ids);
        return cardRepository.findAllById(ids);
    }

    @BatchMapping
    public Map<Card, Account> cards(@Argument List<Card> cards){
        log.info("batchMapping cards={}", cards);
        return cards.stream()
                .collect(java.util.stream.Collectors.toMap(
                        card -> card,
                        card -> accountRepository.findById(card.getAccountId()).orElse(null))
                );
    }

    @SchemaMapping
    public Account account(Card card){
        return accountRepository.findById(card.getAccountId()).orElse(null);
    }

//    @QueryMapping
//    public Card getCard(Long id) {
//        log.info("getCard id={}", id);
//        return cardRepository.findById(id).orElse(null);
//    }
    @QueryMapping
    public List<Card> getAllCards(GraphQLContext graphQLContext, DataFetchingEnvironment environment) {
        log.info("getAllCards");
        return cardRepository.findAll();
    }

    @QueryMapping
    public List<Card> getCards(@Argument String cardHolder){
        log.info("getCards cardHolder={}", cardHolder);
        return cardRepository.findAllByCardHolder(cardHolder);
    }

}
