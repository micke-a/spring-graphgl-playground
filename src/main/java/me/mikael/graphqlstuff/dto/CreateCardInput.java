package me.mikael.graphqlstuff.dto;

import lombok.Data;
import me.mikael.graphqlstuff.model.CardType;

@Data
public class CreateCardInput {
    private Long cardHolderId;
    private Long accountId;
    private CardType cardType;
}
