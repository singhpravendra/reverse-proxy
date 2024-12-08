package com.employee.dao;

import com.employee.bean.ActivityLog;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ActivityLogRepository extends ReactiveMongoRepository<ActivityLog, String> {
}
