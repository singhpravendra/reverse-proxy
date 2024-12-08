package com.employee.controller;

import com.employee.annotation.LogActivity;
import com.employee.bean.Employee;
import com.employee.service.IEmployeeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/employees")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private IEmployeeService employeeService;

    @LogActivity
    @PostMapping
    public Mono<Employee> createEmployee(@RequestBody Employee employee) {
        logger.info("Registering Employee");
        return employeeService.createEmployee(employee);
    }
    @LogActivity
    @PostMapping("/all")
    public Flux<Employee> addMultipleEmployees(@RequestBody List<Employee> employees) {
        logger.info("Registering multiple Employees");
        return employeeService.addMultipleEmployees(employees);
    }

    @LogActivity
    @GetMapping
    public Flux<Employee> getAllEmployees() {
        return employeeService.getAllEmployees();
    }

    @LogActivity
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
