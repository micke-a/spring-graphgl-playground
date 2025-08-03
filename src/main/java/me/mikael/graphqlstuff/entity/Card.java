package me.mikael.graphqlstuff.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.mikael.graphqlstuff.model.CardType;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String cardHolder;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private Long accountId;
}
