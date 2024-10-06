package com.gateway.filters;

import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.data.redis.core.ReactiveStringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.charset.StandardCharsets;
import java.time.Duration;

@Component
public class APIQuotaLimitFilter extends AbstractGatewayFilterFactory<APIQuotaLimitFilter.Config> {

    private final ReactiveStringRedisTemplate redisTemplate;

    public APIQuotaLimitFilter(ReactiveStringRedisTemplate redisTemplate) {
        super(Config.class);
        this.redisTemplate = redisTemplate;
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String clientIp = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            String key = "api_quota:" + clientIp;

            return redisTemplate.opsForValue().increment(key)
                    .flatMap(requestCount -> {
                        if (requestCount > config.getQuotaLimit()) {
                            return handleQuotaExceeded(exchange);
                        } else {
                            if (requestCount == 1) {
                                redisTemplate.expire(key, config.getQuotaDuration()).subscribe();
                            }
                            return chain.filter(exchange);
                        }
                    });
        };
    }

    public static class Config {
        private int quotaLimit = 5;
        private Duration quotaDuration = Duration.ofMinutes(1);

        public int getQuotaLimit() {
            return quotaLimit;
        }

        public void setQuotaLimit(int quotaLimit) {
            this.quotaLimit = quotaLimit;
        }

        public Duration getQuotaDuration() {
            return quotaDuration;
        }

        public void setQuotaDuration(Duration quotaDuration) {
            this.quotaDuration = quotaDuration;
        }
    }

    // Helper method to handle the 429 response when quota is exceeded.
    private Mono<Void> handleQuotaExceeded(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
        byte[] bytes = "API rate limit exceeded".getBytes(StandardCharsets.UTF_8);
        DataBuffer buffer = exchange.getResponse().bufferFactory().wrap(bytes);
        return exchange.getResponse().writeWith(Flux.just(buffer));
    }
}
