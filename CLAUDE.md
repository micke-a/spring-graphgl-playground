# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build & Run Commands

- **Build:** `./mvnw compile`
- **Run:** `./mvnw spring-boot:run`
- **Run tests:** `./mvnw test`
- **Run single test:** `./mvnw test -Dtest=CardControllerTest`
- **Run single test method:** `./mvnw test -Dtest=CardControllerTest#getAllCards_returnsCards`
- **Package:** `./mvnw package`
- **Generate GraphQL client code:** `./mvnw graphqlcodegen:generate` (output: `me.mikael.graphqlstuff.codegen`)

The app runs with an in-memory H2 database seeded by `src/main/resources/data.sql`. GraphiQL UI is available at `/graphiql`.

## Architecture

This is a Spring Boot 3.5 / Java 21 GraphQL playground with **Apollo Federation** support.

### Schema-First GraphQL

The schema is defined in `src/main/resources/graphql/schema.graphqls`. Controllers use Spring's `@QueryMapping`, `@SchemaMapping`, and `@BatchMapping` annotations to resolve fields. Apollo Federation entity resolution uses `@EntityMapping`.

### Domain Model

Three entities with these relationships:
- **Customer** (1) → (N) **Account** (linked via `ownerId`)
- **Customer** (1) → (N) **Card** (linked via `cardHolderId`)
- **Account** (1) → (N) **Card** (linked via `accountId`)

### Key Patterns

- **Batch loading** to avoid N+1 queries: `CardController` uses `@BatchMapping` for account resolution; `AccountController` registers a batch loader via `BatchLoaderRegistry` in `@PostConstruct`.
- **Apollo Federation**: `GraphQlConfiguration` creates a `FederationSchemaFactory` bean and customizes `GraphQlSource` to apply federation directives. Entity resolution is handled by `@EntityMapping` methods in controllers.
- **Header-to-context interceptor**: `HeaderToContextValueGraphQlRequestInterceptor` extracts HTTP headers into `GraphQLContext`, making them available to resolvers.
- **Manual field wiring**: The `Customer.greeting` field is resolved via a `RuntimeWiringConfigurer` bean in `GraphQlConfiguration`, not through a controller.

### Testing

Tests use `@GraphQlTest` for slice testing with `GraphQlTester` and `@MockitoBean` for repositories. Test profile (`application-test.yaml`) adds `federation.graphqls` from test resources to provide federation directive definitions needed by the schema.
