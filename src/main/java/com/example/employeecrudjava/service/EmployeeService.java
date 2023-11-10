package com.example.employeecrudjava.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.example.employeecrudjava.model.Employee;
import com.example.employeecrudjava.repository.EmployeeRepository;

import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public List<Employee> getAll() {
        return employeeRepository.findAll();
    }

    public Employee create(Employee employee) {
        return employeeRepository.save(employee);
    }

    public Long delete(Long id) {
        return employeeRepository.findById(id).map(e -> {
            employeeRepository.delete(e);
            return id;
        }).orElseThrow(() -> notFoundById(id));
    }

    public Long delete(String email) {
        return employeeRepository.findByEmail(email).map(e -> {
            employeeRepository.delete(e);
            return e.getId();
        }).orElseThrow(() -> notFoundByEmail(email));
    }

    private EntityNotFoundException notFoundByEmail(String email) {
        return new EntityNotFoundException(String.format("No Employee with email %s found", email));
    }

    private EntityNotFoundException notFoundById(Long id) {
        return new EntityNotFoundException(String.format("No Employee with Id %d found", id));
    }
}
