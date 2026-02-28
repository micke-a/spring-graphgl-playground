package me.mikael.graphqlstuff.service;

import me.mikael.graphqlstuff.model.CardType;
import me.mikael.graphqlstuff.repository.CardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
@Transactional
class CardServiceIntegrationTest {

    @Autowired
    private CardService cardService;

    @Autowired
    private CardRepository cardRepository;

    @Test
    void deleteCard_existingId_returnsDeletedCard() {
        var payload = cardService.deleteCard(1L);

        assertThat(payload.deletedCard()).isNotNull();
        assertThat(payload.deletedCard().getId()).isEqualTo(1L);
        assertThat(payload.errors()).isEmpty();
    }

    @Test
    void deleteCard_existingId_cardRemovedFromDatabase() {
        cardService.deleteCard(1L);

        assertThat(cardRepository.findById(1L)).isEmpty();
    }

    @Test
    void deleteCard_existingId_returnsCorrectCardData() {
        var payload = cardService.deleteCard(1L);

        var card = payload.deletedCard();
        assertThat(card.getCardHolderId()).isEqualTo(1L);
        assertThat(card.getCardType()).isEqualTo(CardType.VIRTUAL);
        assertThat(card.getAccountId()).isEqualTo(1L);
    }

    @Test
    void deleteCard_existingId_onlyOneCardRemoved() {
        var countBefore = cardRepository.count();

        cardService.deleteCard(1L);

        assertThat(cardRepository.count()).isEqualTo(countBefore - 1);
    }

    @Test
    void deleteCard_nonExistentId_deletedCardIsNull() {
        var payload = cardService.deleteCard(9999L);

        assertThat(payload.deletedCard()).isNull();
    }

    @Test
    void deleteCard_nonExistentId_returnsOneUserError() {
        var payload = cardService.deleteCard(9999L);

        assertThat(payload.errors()).hasSize(1);
    }

    @Test
    void deleteCard_nonExistentId_errorMessageContainsId() {
        var payload = cardService.deleteCard(9999L);

        assertThat(payload.errors().getFirst().getMessage()).contains("9999");
    }

    @Test
    void deleteCard_nonExistentId_databaseUnchanged() {
        var countBefore = cardRepository.count();

        cardService.deleteCard(9999L);

        assertThat(cardRepository.count()).isEqualTo(countBefore);
    }

    @Test
    void deleteCard_nullId_throwsNullPointerException() {
        assertThatThrownBy(() -> cardService.deleteCard(null))
                .isInstanceOf(NullPointerException.class)
                .hasMessage("id must not be null");
    }
}
