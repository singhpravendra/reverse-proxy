package com.analytics.dao;

import com.analytics.bean.UsageData;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UsageDataRepository extends ReactiveMongoRepository<UsageData, String> {
    Flux<UsageData> findByUserId(String userId);
    Mono<UsageData> findByUserIdAndApiEndpoint(String userId, String apiEndpoint);
}
