package me.mikael.graphqlstuff.entity;

import jakarta.persistence.*;
import lombok.*;
import me.mikael.graphqlstuff.model.AccountType;

import java.math.BigDecimal;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@ToString(onlyExplicitlyIncluded = true)
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Include
    @ToString.Include
    private Long id;

    private Long ownerId;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal balance;
}
