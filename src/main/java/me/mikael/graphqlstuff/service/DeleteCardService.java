package me.mikael.graphqlstuff.service;

import lombok.RequiredArgsConstructor;
import me.mikael.graphqlstuff.dto.DeleteCardPayload;
import me.mikael.graphqlstuff.dto.UserError;
import me.mikael.graphqlstuff.repository.CardRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DeleteCardService {

    private final CardRepository cardRepository;

    @Transactional
    public DeleteCardPayload delete(Long cardId) {
        if (!cardRepository.existsById(cardId)) {
            return new DeleteCardPayload(cardId, false,
                List.of(new UserError("Card not found with id: " + cardId,
                    List.of("cardId"))));
        }
        cardRepository.deleteById(cardId);
        return new DeleteCardPayload(cardId, true, List.of());
    }
}
