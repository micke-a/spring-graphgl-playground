package me.mikael.graphqlstuff.service;

import lombok.RequiredArgsConstructor;
import me.mikael.graphqlstuff.dto.DeleteCardPayload;
import me.mikael.graphqlstuff.dto.UserError;
import me.mikael.graphqlstuff.repository.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class CardService {

    private final CardRepository cardRepository;

    @Transactional
    public DeleteCardPayload deleteCard(Long id) {
        Objects.requireNonNull(id, "id must not be null");

        var maybeCard = cardRepository.findById(id);

        if (maybeCard.isEmpty()) {
            var error = new UserError("Card not found with id: " + id, List.of("deleteCard", "id"));
            return new DeleteCardPayload(null, List.of(error));
        }

        var card = maybeCard.get();
        cardRepository.deleteById(id);
        return new DeleteCardPayload(card, List.of());
    }
}
