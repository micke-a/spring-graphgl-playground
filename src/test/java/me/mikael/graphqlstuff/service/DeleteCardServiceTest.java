package me.mikael.graphqlstuff.service;

import me.mikael.graphqlstuff.dto.DeleteCardPayload;
import me.mikael.graphqlstuff.entity.Card;
import me.mikael.graphqlstuff.model.CardType;
import me.mikael.graphqlstuff.repository.CardRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
@ActiveProfiles("test")
class DeleteCardServiceTest {

    @Autowired
    private DeleteCardService deleteCardService;

    @Autowired
    private CardRepository cardRepository;

    @BeforeEach
    void setUp() {
        cardRepository.deleteAll();
    }

    @Test
    void deleteCard_existingId_deletesCardAndReturnsSuccess() {
        Card card = new Card();
        card.setCardHolderId(1L);
        card.setAccountId(10L);
        card.setCardType(CardType.PHYSICAL);
        card = cardRepository.save(card);

        DeleteCardPayload result = deleteCardService.delete(card.getId());

        assertThat(result.isDeleted()).isTrue();
        assertThat(result.getCardId()).isEqualTo(card.getId());
        assertThat(result.getErrors()).isEmpty();
        assertThat(cardRepository.existsById(card.getId())).isFalse();
    }

    @Test
    void deleteCard_nonExistentId_returnsError() {
        DeleteCardPayload result = deleteCardService.delete(999L);

        assertThat(result.isDeleted()).isFalse();
        assertThat(result.getCardId()).isEqualTo(999L);
        assertThat(result.getErrors()).isNotEmpty();
        assertThat(result.getErrors().get(0).getMessage()).contains("999");
    }

    @Test
    void deleteCard_idempotentCallAfterDeletion_returnsError() {
        Card card = new Card();
        card.setCardHolderId(2L);
        card.setAccountId(20L);
        card.setCardType(CardType.VIRTUAL);
        card = cardRepository.save(card);
        Long cardId = card.getId();

        DeleteCardPayload firstResult = deleteCardService.delete(cardId);
        assertThat(firstResult.isDeleted()).isTrue();

        DeleteCardPayload secondResult = deleteCardService.delete(cardId);
        assertThat(secondResult.isDeleted()).isFalse();
        assertThat(secondResult.getErrors()).isNotEmpty();
    }
}
