package com.employee.controller;

import com.employee.bean.Employee;
import com.employee.service.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private IEmployeeService employeeService;

    @PostMapping
    public Mono<Employee> createEmployee(@RequestBody Employee employee) {
        logger.info("Registering Employee");
        return employeeService.createEmployee(employee);
    }

    @GetMapping
    public Flux<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @GetMapping("/{id}")
    public Mono<?> getEmployeeById(@PathVariable String id) {
        logger.info("Get Employee Details by Id {}", id);
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/fetch/employee")
    public Mono<?> getEmployeeId(@RequestParam String id) {
        logger.info("Get All Employee Details");
        return employeeService.getEmployeeById(id);
    }

    @GetMapping("/{id}/with-departments")
    public Mono<?> getEmployeeWithDepartmentByEmpId(@PathVariable String id) {
        logger.info("Get Employee Detail with department");
        return employeeService.getEmployeeWithDepartmentByEmpId(id);
    }
}
