package me.mikael.graphqlstuff.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import me.mikael.graphqlstuff.model.AccountType;

import java.math.BigDecimal;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String owner;

    @Enumerated(EnumType.STRING)
    private AccountType accountType;

    private BigDecimal balance;
}
