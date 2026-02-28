package me.mikael.graphqlstuff.dto;

import lombok.Data;
import me.mikael.graphqlstuff.model.AccountType;

import java.math.BigDecimal;

@Data
public class CreateAccountInput {
    private Long ownerId;
    private AccountType accountType;
    private BigDecimal balance;
}
