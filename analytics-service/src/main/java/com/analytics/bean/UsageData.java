package com.analytics.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@Document(collection = "api_usage")
public class UsageData {

    @Setter
    @Id
    private String id;
    private String userId;
    private String apiEndpoint;
    private int callCount;

    public UsageData() {
    }

    public UsageData(String userId, String apiEndpoint) {
        this.userId = userId;
        this.apiEndpoint = apiEndpoint;
        this.callCount = 1; // Initial call
    }

    public void incrementUsage() {
        this.callCount += 1;
    }
}
