package com.employee.filters;

import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebFilter;
import org.springframework.web.server.WebFilterChain;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

@Component
public class TraceIdFilter implements WebFilter {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, WebFilterChain chain) {
        // Get traceId from request header (or generate a new one)
        String traceId = exchange.getRequest().getHeaders().getFirst("x-trace-id");
        if (traceId == null) {
            traceId = generateTraceId();  // Generate a new TraceId if it's not provided
        }

        // Store the traceId in the context
        //chain.filter(exchange).contextWrite(Context.of("x-trace-id", traceId));
        return chain.filter(exchange)
                .contextWrite(Context.of(ServerWebExchange.class, exchange, "x-trace-id", traceId));  // Inject ServerWebExchange into the context
    }

    private String generateTraceId() {
        // Your traceId generation logic
        return "TRACE-" + System.currentTimeMillis();
    }
}