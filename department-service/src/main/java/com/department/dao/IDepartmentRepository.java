package com.department.dao;

import com.department.bean.Department;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface IDepartmentRepository extends ReactiveMongoRepository<Department, String> {
}
