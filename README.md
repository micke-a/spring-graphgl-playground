
Simple project exploring what it is like to work with GraphQL using Spring Boot

## Useful links
* https://docs.spring.io/spring-boot/reference/web/spring-graphql.html
* https://docs.spring.io/spring-graphql/reference/federation.html
* https://medium.com/miragon/how-to-access-http-headers-in-spring-for-graphql-be166bd33312
* https://blog.softwaremill.com/graphql-dataloader-in-spring-boot-singleton-or-request-scoped-16699436f680
* https://compositecode.blog/2023/06/20/dataloader-for-graphql-implementations/

## GraphiQL

http://localhost:8080/graphiql

### Example queries

```graphql
{
    getAllCards {
        id
        cardHolder
        accountId
    }
    getAllAccounts {
        id
        owner
        accountType
    }
    getCards(cardHolder: "jane") {
        id
        cardHolder
        cardType
        account {
            id
            owner
            accountType
            balance
        }
    }
    accountById: getAccount(id: 2) {
        id
        owner
    }
    accountByOwner: getAccount(owner: "jane") {
        id
        owner
    }
}
```

```graphql
{
  getAllCards {
    id
    cardHolder
    account{
        id
        owner
        balance
    }
  }
}

```

## Misc notes

### What are Entity types
https://www.apollographql.com/docs/graphos/schema-design/federated-schemas/entities/intro

Apparently this is any Object type which has a `@key` directive in their definition.
These `@key` fields(s) must uniquely identify an instance of an Object. 
Sounds the same as the primary key on a Database table.


Example from docs above

```graphql
type Product @key(fields: "upc") {
  upc: ID!
  name: String!
  price: Int
}

type Product @key(fields: "productUpc") {
    productUpc: ID!
    rating: Int!
}
```



### Accessing Header Values

You can implement something like `HeaderToContextValueGraphQlRequestInterceptor` which puts then into the GraphQLContext.
After which they can be accessed from the `GraphQLContext` object, which can be added as an argument to any QueryMapping method.

Or `@ContectValue` can also be used to pluck specific header values out of the context and passed into methods as arguments.

### Federation 

Got this error when strating to try and figure out how this works

```shell
Caused by: java.lang.NoClassDefFoundError: com/apollographql/federation/graphqljava/SchemaTransformer
```