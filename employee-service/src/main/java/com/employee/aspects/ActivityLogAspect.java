package com.employee.aspects;

import com.employee.annotation.LogActivity;
import com.employee.service.ActivityLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Aspect
@Component
public class ActivityLogAspect {

    private final ActivityLogService loggerUtil;

    public ActivityLogAspect(ActivityLogService loggerUtil) {
        this.loggerUtil = loggerUtil;
    }

    @Around("@annotation(logActivity)")
    public Object logActivity(ProceedingJoinPoint joinPoint, LogActivity logActivity) throws Throwable {
        // Extract method arguments (request)
        String requestPayload = Arrays.toString(joinPoint.getArgs());

        try {
            // Proceed with the method execution and handle reactive types
            Object result = joinPoint.proceed();

            if (result instanceof Mono<?> monoResult) {
                return logMono(monoResult, requestPayload);
            } else if (result instanceof Flux<?> fluxResult) {
                return logFlux(fluxResult, requestPayload);
            }

            return result; // For non-reactive return types
        } catch (Exception e) {
            // Log the failure scenario
            logFailure(e);
            throw e; // Re-throw the exception after logging
        }
    }

    private Mono<?> logMono(Mono<?> monoResult, String requestPayload) {
        return Mono.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("x-trace-id", "UNKNOWN_TRACE_ID");
            ServerWebExchange exchange = ctx.get(ServerWebExchange.class);

            logRequest(traceId, exchange, requestPayload);
            return monoResult
                    .doOnError(e -> logFailure(e)) // Log the error if Mono fails
                    .flatMap(response -> {
                        logResponse(traceId, response.toString());
                        return Mono.just(response);
                    });
        });
    }

    private Flux<?> logFlux(Flux<?> fluxResult, String requestPayload) {
        return Flux.deferContextual(ctx -> {
            String traceId = ctx.getOrDefault("x-trace-id", "UNKNOWN_TRACE_ID");
            ServerWebExchange exchange = ctx.get(ServerWebExchange.class);

            logRequest(traceId, exchange, requestPayload);
            return fluxResult
                    .doOnError(e -> logFailure(e)) // Log the error if Flux fails
                    .collectList()
                    .flatMap(responses -> {
                        logResponse(traceId, responses.toString());
                        return Mono.just(responses);
                    });
        });
    }

    private void logRequest(String traceId, ServerWebExchange exchange, String body) {
        Map<String, Object> requestData = new HashMap<>();
        requestData.put("method", exchange.getRequest().getMethod().toString());
        requestData.put("path", exchange.getRequest().getURI().getPath());
        requestData.put("queryParams", exchange.getRequest().getQueryParams());
        requestData.put("headers", exchange.getRequest().getHeaders());
        requestData.put("body", body);

        loggerUtil.log(traceId, requestData, null).subscribe();
    }

    private void logResponse(String traceId, Object responsePayload) {
        loggerUtil.log(traceId, null, responsePayload).subscribe();
    }

    private void logFailure(Throwable throwable) {
        Map<String, Object> errorData = new HashMap<>();
        errorData.put("error", throwable.getMessage());
        errorData.put("stackTrace", throwable.getStackTrace());

        loggerUtil.log("UNKNOWN_TRACE_ID", null, errorData).subscribe();
    }
}
