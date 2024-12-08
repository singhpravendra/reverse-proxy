package com.department.service.impl;

import com.department.bean.TraceEntity;
import com.department.dao.TraceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
public class TraceService {

    @Autowired
    private final TraceRepository traceRepository;
    //private final Encryptor encryptor;

    @Autowired
    public TraceService(TraceRepository traceRepository) {
        this.traceRepository = traceRepository;
        //this.encryptor = encryptor;
    }

    public void saveTrace(String traceId, String requestPayload, String responsePayload) {
        // Encrypt request and response payloads
        //String encryptedRequest = encryptor.encrypt(requestPayload);
        //String encryptedResponse = encryptor.encrypt(responsePayload);

        // Create trace entity and save it
        TraceEntity traceEntity = new TraceEntity(traceId, requestPayload, responsePayload, Instant.now());
        traceRepository.save(traceEntity);
    }
}
