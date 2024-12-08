package com.department.dao;

import com.department.bean.TraceEntity;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface TraceRepository extends ReactiveMongoRepository<TraceEntity, String> {
}
