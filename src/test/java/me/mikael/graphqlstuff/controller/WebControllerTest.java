package me.mikael.graphqlstuff.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class WebControllerTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void homePageRendersSuccessfully() {
        ResponseEntity<String> response = restTemplate.getForEntity("/", String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        String body = response.getBody();
        assertThat(body).isNotNull();

        // Base layout elements
        assertThat(body).contains("<!DOCTYPE html>");
        assertThat(body).contains("<title>Home - GraphQL Playground</title>");
        assertThat(body).contains("bootstrap@5.3.3");
        assertThat(body).contains("alpinejs");
        assertThat(body).contains("function graphqlFetch");

        // Navbar links
        assertThat(body).contains("href=\"/\"");
        assertThat(body).contains("href=\"/customers\"");
        assertThat(body).contains("href=\"/accounts\"");
        assertThat(body).contains("href=\"/cards\"");
        assertThat(body).contains("href=\"/graphiql\"");

        // Home page content
        assertThat(body).contains("Dashboard</h1>");

        // Stat cards
        assertThat(body).contains("<h5 class=\"card-title\">Customers</h5>");
        assertThat(body).contains("<h5 class=\"card-title\">Accounts</h5>");
        assertThat(body).contains("<h5 class=\"card-title\">Cards</h5>");
        assertThat(body).contains("<h5 class=\"card-title\">Total Balance</h5>");

        // Recent customers table
        assertThat(body).contains("Recent Customers");

        // Alpine.js data binding
        assertThat(body).contains("x-data");
        assertThat(body).contains("async init()");
        assertThat(body).contains("getAllCustomers");
        assertThat(body).contains("getAllAccounts");
        assertThat(body).contains("getAllCards");

        // New Customer button and modal
        assertThat(body).contains("New Customer");
        assertThat(body).contains("createCustomer()");
        assertThat(body).contains("createCustomerAccountCard");
        assertThat(body).contains("id=\"customerName\"");
    }
}
