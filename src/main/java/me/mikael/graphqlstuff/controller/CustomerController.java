package me.mikael.graphqlstuff.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.mikael.graphqlstuff.entity.Customer;
import me.mikael.graphqlstuff.repository.CustomerRepository;
import org.springframework.graphql.data.method.annotation.Argument;
import org.springframework.graphql.data.method.annotation.QueryMapping;
import org.springframework.stereotype.Controller;

import java.util.List;

@Slf4j
@Controller
@RequiredArgsConstructor
public class CustomerController {
    private final CustomerRepository cardholderRepository;


    @QueryMapping
    public List<Customer> getAllCustomers(){
        log.info("getAllCardholders");
        return cardholderRepository.findAll();
    }

    @QueryMapping
    public Customer getCustomer(@Argument Long id){
        log.info("getCardholder id={}", id);
        return cardholderRepository.findById(id).orElse(null);
    }
}
