package me.mikael.graphqlstuff.configuration;

import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.federation.FederationSchemaFactory;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;
import me.mikael.graphqlstuff.entity.Customer;

import java.util.Optional;

@Configuration
public class GraphQlConfiguration {

    // Exmple adding a custom field and wiring the logic manually
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer() {
        return wiringBuilder -> wiringBuilder
                .type("Customer", builder -> builder
                        .dataFetcher("greeting", env -> {
                            Customer customer = env.getSource();
                            return "Hello, "
                                    + Optional.ofNullable(customer)
                                    .map(Customer::getName)
                                    .orElse("Anonymous")
                                    + "!";
                        })
                );
    }

    @Bean
    public GraphQlSourceBuilderCustomizer customizer(FederationSchemaFactory federationSchemaFactory) {
        return builder -> builder.schemaFactory(federationSchemaFactory::createGraphQLSchema);
    }

    @Bean
    public FederationSchemaFactory federationSchemaFactory() {
        return new FederationSchemaFactory();
    }
}
