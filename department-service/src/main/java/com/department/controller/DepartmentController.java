package com.department.controller;

import com.department.bean.Department;
import com.department.service.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/departments")
public class DepartmentController {


    @Autowired
    private IDepartmentService departmentService;

    @PostMapping
    public Mono<Department> createDepartment(@RequestBody Department department) {
        return departmentService.createDepartment(department);
    }

    @GetMapping
    public Flux<Department> getAllEmployees() {
        return departmentService.getAllDepartment();
    }

    @GetMapping("/{id}")
    public Mono<?> getDepartmentById(@PathVariable String id) {
        return departmentService.getDepartmentById(id);
    }
}
