package me.mikael.graphqlstuff.controller;


import me.mikael.graphqlstuff.entity.Card;
import me.mikael.graphqlstuff.model.CardType;
import me.mikael.graphqlstuff.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.graphql.GraphQlTest;
import org.springframework.graphql.test.tester.GraphQlTester;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;

import static org.mockito.Mockito.when;

@GraphQlTest(CardController.class)
class CardControllerTest {

    @Autowired
    private GraphQlTester graphQlTester;

    @MockitoBean
    private CardRepository cardRepository;

    @Test
    void getAllCards_returnsCards() {
        when(cardRepository.findAll()).thenReturn(List.of(
                new Card(1L, "alice", CardType.PHYSICAL, 10L),
                new Card(2L, "bob", CardType.VIRTUAL, 11L)
        ));

        graphQlTester.document("{ getAllCards { id cardHolder cardType accountId } }")
                .execute()
                .path("getAllCards")
                .entityList(Card.class)
                .hasSize(2);
    }
}