package com.employee.service;

import com.employee.bean.ActivityLog;
import com.employee.dao.ActivityLogRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class ActivityLogService {

    private final ActivityLogRepository repository;

    public ActivityLogService(ActivityLogRepository repository) {
        this.repository = repository;
    }

    public Mono<Void> log(String traceId, Object request, Object response) {
        ActivityLog log = new ActivityLog();
        log.setTraceId(traceId);
        log.setRequest(request);
        log.setResponse(response);
        log.setTimestamp(Instant.now().toString());

        return repository.save(log).then();
    }
}
