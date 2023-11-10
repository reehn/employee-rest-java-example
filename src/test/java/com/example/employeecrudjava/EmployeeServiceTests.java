package com.example.employeecrudjava;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import com.example.employeecrudjava.model.Employee;
import com.example.employeecrudjava.repository.EmployeeRepository;
import com.example.employeecrudjava.service.EmployeeService;
import com.example.employeecrudjava.testUtils.TestUtils;

import jakarta.persistence.EntityNotFoundException;

@SpringBootTest
public class EmployeeServiceTests {

    @Autowired
    private EmployeeService employeeService;

    @MockBean
    private EmployeeRepository employeeRepository;

    static Stream<Arguments> getAllEmployees() {
        return Stream.of(
                Arguments.of(List.of(
                        new Employee(1L, "test", "testsson", "test@test.se"),
                        new Employee(2L, "test", "testsson", "test2@test.se"))),
                Arguments.of(
                        List.of(TestUtils.getTestEmployee())),
                Arguments.of(new ArrayList<>()));
    }

    @ParameterizedTest
    @MethodSource("getAllEmployees")
    void testGetAllEmployees(List<Employee> employees) {
        when(employeeRepository.findAll()).thenReturn(employees);

        assertEquals(employees, employeeService.getAll());
    }

    @Test
    void testCreateEmployee() {
        Employee employee = TestUtils.getTestEmployee();

        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.ofNullable(null));
        when(employeeRepository.save(employee)).thenReturn(employee);

        Employee toBeCreated = TestUtils.getTestEmployee();

        assertEquals(employeeService.create(toBeCreated), toBeCreated);
    }

    @Test
    void testDeleteEmployeeById() {
        Employee employee = TestUtils.getTestEmployee();

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.of(employee));

        assertEquals(1, employeeService.delete(employee.getId()));

        when(employeeRepository.findById(employee.getId())).thenReturn(Optional.ofNullable(null));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.delete(employee.getId());
        });

        assertEquals("No Employee with Id 1 found", exception.getMessage());
    }

    @Test
    void testDeleteEmployeeByEmail() {
        Employee employee = TestUtils.getTestEmployee();

        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.of(employee));

        assertEquals(1, employeeService.delete(employee.getEmail()));

        when(employeeRepository.findByEmail(employee.getEmail())).thenReturn(Optional.ofNullable(null));

        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            employeeService.delete(employee.getEmail());
        });

        assertEquals("No Employee with email kalle@anka.se found", exception.getMessage());
    }

}
