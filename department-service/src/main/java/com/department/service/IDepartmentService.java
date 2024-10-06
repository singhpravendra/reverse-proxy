package com.department.service;

import com.department.bean.Department;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public interface IDepartmentService {
    Mono<Department> createDepartment(Department department);

    Mono<Department> getDepartmentById(String id);

    Flux<Department> getAllDepartment();

}
