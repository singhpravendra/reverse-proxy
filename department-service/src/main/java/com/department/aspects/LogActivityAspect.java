package com.department.aspects;

import com.department.bean.TraceEntity;
import com.department.dao.TraceRepository;
import org.aspectj.lang.annotation.*;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Aspect
@Component
public class LogActivityAspect {

    private final TraceRepository mongoTemplate;

    @Autowired
    public LogActivityAspect(TraceRepository mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Around("@annotation(com.department.annotations.LogActivity)")
    public Object logActivity(ProceedingJoinPoint joinPoint) {
        // Generate traceId for the request
        String traceId = "12343";

        // Use Mono.deferContextual to make sure we get the ServerWebExchange in the reactive context
        return Mono.deferContextual(context -> {
            System.out.println(context);
            ServerWebExchange exchange = context.get(ServerWebExchange.class);

            // Log incoming request details asynchronously
            return logRequestDetails(traceId, exchange)
                    .then(Mono.defer(() -> {
                        // Proceed with the actual method call reactively
                        return Mono.fromCallable(() -> {
                                    try {
                                        return joinPoint.proceed();
                                    } catch (Throwable e) {
                                        throw new RuntimeException(e);
                                    }
                                })
                                .flatMap(result -> {
                                    // After method execution, log response details and save to Mongo
                                    logResponseDetails(traceId, exchange);
                                    return saveLogToMongo(traceId, exchange);
                                });
                    }));
        });
    }

    // Log incoming request details asynchronously
    private Mono<Void> logRequestDetails(String traceId, ServerWebExchange exchange) {
        String method = exchange.getRequest().getMethod().toString();
        String path = exchange.getRequest().getURI().getPath();
        String headers = exchange.getRequest().getHeaders().toString();
        String queryParams = exchange.getRequest().getQueryParams().toString();

        // Log request details (you can log to a file, console, or external system)
        System.out.println("TraceId: " + traceId + ", Request - Method: " + method + ", Path: " + path +
                ", Headers: " + headers + ", QueryParams: " + queryParams);

        return Mono.empty(); // Return Mono to keep it non-blocking
    }

    // Log outgoing response details asynchronously
    private void logResponseDetails(String traceId, ServerWebExchange exchange) {
        String status = exchange.getResponse().getStatusCode().toString();
        String responseBody = "Response data"; // This would depend on your actual implementation

        // Log response details
        System.out.println("TraceId: " + traceId + ", Response - Status: " + status + ", Body: " + responseBody);
    }

    // Save log to MongoDB reactively
    private Mono<Void> saveLogToMongo(String traceId, ServerWebExchange exchange) {
        TraceEntity log = new TraceEntity();
        log.setTraceId(traceId);
        log.setRequestPayload("Request: " + exchange.getRequest().getURI().getPath());
        log.setResponsePayload("Response: Sample Response"); // Customize with actual response data

        // Save the log to MongoDB reactively
        return Mono.fromCallable(() -> mongoTemplate.save(log))
                .then();
    }
}
