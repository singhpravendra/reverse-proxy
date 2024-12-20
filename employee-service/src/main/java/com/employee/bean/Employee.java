package com.employee.bean;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Document(collection = "employee")
@Data
public class Employee {

    @Id
    private String id;
    private String name;
    private String departmentId;

}
