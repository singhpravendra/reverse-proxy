package com.gateway.filters;

import com.gateway.bean.UsageData;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class MonetizationFilter extends AbstractGatewayFilterFactory<MonetizationFilter.Config> {

    private final WebClient.Builder webClientBuilder;

    public MonetizationFilter(WebClient.Builder webClientBuilder) {
        super(Config.class);
        this.webClientBuilder = webClientBuilder;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> chain.filter(exchange).then(Mono.fromRunnable(() -> {
            String userId = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            String serviceName = exchange.getRequest().getURI().getPath().split("/")[1]; // e.g., 'employee'
            String apiEndpoint = exchange.getRequest().getURI().getPath();

            UsageData record = new UsageData(userId, serviceName, apiEndpoint, System.currentTimeMillis());

            // Send the usage data to the monetization microservice
            webClientBuilder.build().post()
                    .uri("http://analytics-service/monetization/track-usage")
                    .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .bodyValue(record)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .subscribe();

        }));

    }

    public static class Config {
        // Configuration properties (if any) can be added here
    }
}
