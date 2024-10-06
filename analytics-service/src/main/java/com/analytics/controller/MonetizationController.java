package com.analytics.controller;

import com.analytics.bean.UsageData;
import com.analytics.dao.UsageDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/monetization")
public class MonetizationController {

    @Autowired
    private UsageDataRepository usageDataRepository;

    @PostMapping("/track-usage")
    public Mono<Void> trackUsage(@RequestBody UsageData usageData) {
        return usageDataRepository.findByUserIdAndApiEndpoint(usageData.getUserId(), usageData.getApiEndpoint())
                .flatMap(existingUsage -> {
                    existingUsage.incrementUsage();
                    return usageDataRepository.save(existingUsage);
                })
                .switchIfEmpty(usageDataRepository.save(new UsageData(usageData.getUserId(), usageData.getApiEndpoint())))
                .then();
    }

    @GetMapping("/usage-report")
    public Flux<UsageData> getUsageReport(@RequestParam String username) {
        return usageDataRepository.findByUserId(username);
    }
}
