package com.gateway.bean;

import java.io.Serializable;

public class UsageData implements Serializable {

    private String userId;
    private String serviceName;
    private String apiEndpoint;
    private long requestTimestamp;

    public UsageData(String userId, String serviceName, String apiEndpoint, long requestTimestamp) {
        this.userId = userId;
        this.serviceName = serviceName;
        this.apiEndpoint = apiEndpoint;
        this.requestTimestamp = requestTimestamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }

    public String getApiEndpoint() {
        return apiEndpoint;
    }

    public void setApiEndpoint(String apiEndpoint) {
        this.apiEndpoint = apiEndpoint;
    }

    public long getRequestTimestamp() {
        return requestTimestamp;
    }

    public void setRequestTimestamp(long requestTimestamp) {
        this.requestTimestamp = requestTimestamp;
    }
}
