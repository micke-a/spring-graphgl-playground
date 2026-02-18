package me.mikael.graphqlstuff.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mikael.graphqlstuff.model.CardType;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Card {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    private Long cardHolderId;

    @Enumerated(EnumType.STRING)
    private CardType cardType;

    private Long accountId;
}
