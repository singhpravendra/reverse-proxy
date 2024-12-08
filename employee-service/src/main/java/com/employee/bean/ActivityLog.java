package com.employee.bean;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "activity_logs")
@Data
public class ActivityLog {
    @Id
    private String id;
    private String traceId;
    private Object request;
    private Object response;
    private String timestamp;
}
