package me.mikael.graphqlstuff;

import lombok.extern.slf4j.Slf4j;
import org.springframework.graphql.server.WebGraphQlInterceptor;
import org.springframework.graphql.server.WebGraphQlRequest;
import org.springframework.graphql.server.WebGraphQlResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.TreeMap;

/**
 * This class simply takes all incoming request headers and puts them into the GrahpQL Context as is.
 *
 * This kind of interceptor looks quite nice for cross-cutting security concerns
 * - Forcing strong customer authentication
 * - Surfacing JWT Claims as Context values
 */
@Slf4j
@Component
public class HeaderToContextValueGraphQlRequestInterceptor implements WebGraphQlInterceptor {
    @Override
    public Mono<WebGraphQlResponse> intercept(WebGraphQlRequest request, Chain chain) {
        var headers = new TreeMap<String,Object>();
        for(var headerName : request.getHeaders().keySet()){
            headers.put(headerName, request.getHeaders().get(headerName));
        }
        log.info("Adding {} headers to the GQL-context", headers);
        request.configureExecutionInput((execInput, builder)
                -> builder.graphQLContext(headers).build()
        );
        return chain.next(request);
    }
}
