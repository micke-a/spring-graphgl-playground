package me.mikael.graphqlstuff.dto;

import me.mikael.graphqlstuff.entity.Card;

import java.util.List;

public record DeleteCardPayload(Card deletedCard, List<UserError> errors) {}
