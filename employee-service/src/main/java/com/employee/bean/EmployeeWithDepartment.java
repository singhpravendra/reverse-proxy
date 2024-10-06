package com.employee.bean;

import lombok.Data;

@Data
public class EmployeeWithDepartment {
    private Employee employee;
    private Object department;
}
