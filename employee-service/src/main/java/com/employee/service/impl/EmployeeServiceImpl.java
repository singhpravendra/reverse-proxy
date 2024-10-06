package com.employee.service.impl;

import com.employee.bean.Employee;
import com.employee.dao.IEmployeeRepository;
import com.employee.service.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class EmployeeServiceImpl implements IEmployeeService {

    @Autowired
    private WebClient.Builder webClient;
    @Autowired
    private IEmployeeRepository iEmployeeRepository;

    @Override
    public Mono<Employee> createEmployee(Employee employee) {
        return iEmployeeRepository.save(employee);
    }

    @Override
    public Mono<Employee> getEmployeeById(String id) {
        return iEmployeeRepository.findById(id);
    }

    @Override
    public Flux<Employee> getAllEmployees() {
        return iEmployeeRepository.findAll();
    }

    @Override
    public Mono<?> getEmployeeWithDepartmentByEmpId(String id) {
        return getEmployeeById(id)
                .flatMap(employee -> webClient.build()
                        .get()
                        .uri("http://localhost:8085/departments/{departmentId}", employee.getDepartmentId())
                        //.uri("http://DEPARTMENT-SERVICE/departments/{departmentId}", employee.getDepartmentId())
                        .retrieve()
                        .bodyToMono(Object.class)
                        .map(department -> Map.of("employee", employee, "department", department)));
    }
}
