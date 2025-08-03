package me.mikael.graphqlstuff.configuration;

import org.springframework.boot.autoconfigure.graphql.GraphQlSourceBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.data.federation.FederationSchemaFactory;

@Configuration
public class GraphQlConfiguration {

    @Bean
    public GraphQlSourceBuilderCustomizer customizer(FederationSchemaFactory federationSchemaFactory) {
        return builder -> builder.schemaFactory(federationSchemaFactory::createGraphQLSchema);
    }

    @Bean
    public FederationSchemaFactory federationSchemaFactory() {
        return new FederationSchemaFactory();
    }
}
