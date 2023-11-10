package com.example.employeecrudjava.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.example.employeecrudjava.model.Employee;
import com.example.employeecrudjava.service.EmployeeService;

import lombok.AllArgsConstructor;

@RestController
@AllArgsConstructor
@RequestMapping("/employees")
public class EmployeeController {

    private final EmployeeService employeeService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping
    public List<Employee> getAllEmployees() {
        return employeeService.getAll();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public Employee createEmployee(@Validated @RequestBody Employee employee) {
        return employeeService.create(employee);
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/id/{id}")
    public String deleteEmployee(@PathVariable Long id) {
        return (String.format("Successfully deleted Employee with Id %d", employeeService.delete(id)));
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/delete/email/{email}")
    public String deleteEmployee(@PathVariable String email) {
        return String.format("Successfully deleted Employee with Id %s and email %s",
                employeeService.delete(email), email);
    }
}
