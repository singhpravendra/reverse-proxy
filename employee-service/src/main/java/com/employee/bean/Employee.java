package com.employee.bean;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "employee")
@Setter
@Getter
public class Employee {

    @Id
    private String id;
    private String name;
    private String departmentId;

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartmentId() {
        return departmentId;
    }
}
