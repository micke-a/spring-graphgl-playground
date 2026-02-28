package me.mikael.graphqlstuff.controller;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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

        var body = response.getBody();
        assertThat(body).isNotNull();

        var doc = Jsoup.parse(body);

        // Base layout elements
        assertThat(doc.title()).isEqualTo("Home - GraphQL Playground");
        assertThat(doc.select("link[href*=bootstrap@5.3.3]")).isNotEmpty();
        assertThat(doc.select("script[src*=alpinejs]")).isNotEmpty();
        assertThat(doc.select("script").stream()
                .anyMatch(el -> el.data().contains("function graphqlFetch"))).isTrue();

        // Navbar links
        assertThat(doc.select("nav a[href=/]")).isNotEmpty();
        assertThat(doc.select("nav a[href=/customers]")).isNotEmpty();
        assertThat(doc.select("nav a[href=/accounts]")).isNotEmpty();
        assertThat(doc.select("nav a[href=/cards]")).isNotEmpty();
        assertThat(doc.select("nav a[href=/graphiql]")).isNotEmpty();

        // Home page content
        assertThat(doc.select("h1:containsOwn(Dashboard)")).isNotEmpty();

        // Stat cards
        assertThat(doc.select("h5.card-title:containsOwn(Customers)")).isNotEmpty();
        assertThat(doc.select("h5.card-title:containsOwn(Accounts)")).isNotEmpty();
        assertThat(doc.select("h5.card-title:containsOwn(Cards)")).isNotEmpty();
        assertThat(doc.select("h5.card-title:containsOwn(Total Balance)")).isNotEmpty();

        // Recent customers table
        assertThat(doc.select("h3:containsOwn(Recent Customers)")).isNotEmpty();

        // Alpine.js data binding
        var xDataElements = doc.select("[x-data]");
        assertThat(xDataElements).isNotEmpty();
        var xDataValue = xDataElements.first().attr("x-data");
        assertThat(xDataValue).contains("async init()");
        assertThat(xDataValue).contains("getAllCustomers");
        assertThat(xDataValue).contains("getAllAccounts");
        assertThat(xDataValue).contains("getAllCards");

        // New Customer button and modal
        assertThat(doc.select("button:containsOwn(New Customer)")).isNotEmpty();
        assertThat(doc.select(".modal")).isNotEmpty();
        assertThat(doc.select("#customerName")).isNotEmpty();
        assertThat(xDataValue).contains("createCustomer()");
        assertThat(xDataValue).contains("createCustomerAccountCard");
    }
}
