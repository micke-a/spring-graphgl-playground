package me.mikael.graphqlstuff.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import java.util.List;

@Getter
@AllArgsConstructor
public class DeleteCardPayload {
    private final Long cardId;
    private final boolean deleted;
    private final List<UserError> errors;
}
