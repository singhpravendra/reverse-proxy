package com.employee.dao;

import com.employee.bean.Employee;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface IEmployeeRepository extends ReactiveMongoRepository<Employee, String> {
}
