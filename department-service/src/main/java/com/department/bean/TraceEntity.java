package com.department.bean;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.Instant;

@Document(collection = "traces")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class TraceEntity {

    @Id
    private String traceId;
    private String requestPayload;
    private String responsePayload;
    private Instant createdAt;

}
