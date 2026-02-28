package me.mikael.graphqlstuff.dto;

import lombok.Data;

@Data
public class CreateCustomerAccountCardInput {
    private CreateCustomerInput customer;
    private CreateAccountInput account;
    private CreateCardInput card;
}
