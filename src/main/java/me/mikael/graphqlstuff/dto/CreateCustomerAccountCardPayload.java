package me.mikael.graphqlstuff.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import me.mikael.graphqlstuff.entity.Account;
import me.mikael.graphqlstuff.entity.Card;
import me.mikael.graphqlstuff.entity.Customer;

import java.util.List;

@Data
@AllArgsConstructor
public class CreateCustomerAccountCardPayload {
    private Customer customer;
    private Account account;
    private Card card;
    private List<UserError> errors;
}
