package com.employee.util;

import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class ActivityTraceUtil {
    public Mono<String> getTraceId() {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("x-trace-id", "UNKNOWN_TRACE_ID");
            assert traceId != null;
            return Mono.just(traceId);
        });

    }
}
