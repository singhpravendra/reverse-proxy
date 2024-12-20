package com.employee.service;

import com.employee.bean.Employee;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@Service
public interface IEmployeeService {
    Mono<Employee> createEmployee(Employee employee);

    Mono<Employee> getEmployeeById(String id);

    Flux<Employee> getAllEmployees();

    Mono<?> getEmployeeWithDepartmentByEmpId(String id);

    Flux<Employee> addMultipleEmployees(List<Employee> employees);
}
