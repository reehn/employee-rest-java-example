package com.example.employeecrudjava;

import static org.mockito.Mockito.when;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultMatcher;

import com.example.employeecrudjava.controller.EmployeeController;
import com.example.employeecrudjava.service.EmployeeService;
import com.example.employeecrudjava.testUtils.TestUtils;

import jakarta.persistence.EntityNotFoundException;

@WebMvcTest(EmployeeController.class)
@AutoConfigureMockMvc
public class EmployeeApiTests {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testCallingGetAllEmployees() throws Exception {
    
        when(employeeService.getAll()).thenReturn(List.of(TestUtils.getTestEmployee()));

        mockMvc.perform(get("/employees")
            .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.[*]").exists())
        .andExpect(jsonPath("$.[*].id").isNotEmpty())
        .andExpect(jsonPath("$.[*].firstName").isNotEmpty())
        .andExpect(jsonPath("$.[*].lastName").isNotEmpty())
        .andExpect(jsonPath("$.[*].email").isNotEmpty());

    }

    static Stream<Arguments> createEmployees() {
        return Stream.of(
                Arguments.of(TestUtils.asJsonString(TestUtils.getTestEmployee("Kalle", "Anka", "kalle@anka.se")), true,
                        status().isCreated(), jsonPath("$.id").exists()),
                Arguments.of(TestUtils.asJsonString(TestUtils.getTestEmployee("", "Anka", "kalle@anka.se")), false,
                       status().isBadRequest(), jsonPath("$.detailedMessage").value(
                            "Field firstName is required and must be between 1 and 30 characters long")),
                Arguments.of("""
                            {
                            "firstName": null,
                            "lastName": "Anka",
                            "email": "kalle@anka.se"
                            }
                            """, false,
                       status().isBadRequest(), jsonPath("$.detailedMessage").value(
                            "JSON parse error: firstName is marked non-null but is null")),
                Arguments.of(TestUtils.asJsonString(TestUtils.getTestEmployee("Kalle", "Anka", "")), false,
                       status().isBadRequest(), jsonPath("$.detailedMessage").value(
                            "Email is required and must be a valid email-address")),
                Arguments.of("""
                            {
                            "firstName": "Kalle",
                            "lastName": "Anka",
                            "email": "null"
                            }
                            """, false,
                       status().isBadRequest(), jsonPath("$.detailedMessage").value(
                            "Email is required and must be a valid email-address")),
                Arguments.of(TestUtils.asJsonString(TestUtils.getTestEmployee("Kalle", "Anka", "kalle-with-invalid-email")), false,
                       status().isBadRequest(), jsonPath("$.detailedMessage").value(
                            "Email is required and must be a valid email-address")));
    }

    @ParameterizedTest
    @MethodSource("createEmployees")
    void testCreateEmployee(String employeeToCreate, boolean isValid, ResultMatcher statusResult, ResultMatcher expectedValue) throws Exception {

        if (isValid) {
            when(employeeService.create(TestUtils.getTestEmployee("Kalle", "Anka", "kalle@anka.se")))
                    .thenReturn(TestUtils.getTestEmployee());

            mockMvc.perform(post("/employees")
                    .content(employeeToCreate)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(statusResult)
                    .andExpect(expectedValue);

        } else {

            mockMvc.perform(post("/employees")
                    .content(employeeToCreate)
                    .contentType(MediaType.APPLICATION_JSON))
                    .andExpect(statusResult)
                    .andExpect(expectedValue);
        }
    }

    @Test
    void testDeleteEmployeeById() throws Exception {

        when(employeeService.delete(1L)).thenReturn(1L);

        mockMvc.perform(delete("/employees/delete/id/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("Successfully deleted Employee with Id 1"));


        when(employeeService.delete(1L)).thenThrow(new EntityNotFoundException("No Employee with Id 1 found"));

        mockMvc.perform(delete("/employees/delete/id/1")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.detailedMessage").value("No Employee with Id 1 found"));
    }

    @Test
    void testDeleteEmployeeByEmail() throws Exception {

        when(employeeService.delete("kalle@anka.se")).thenReturn(1L);

        mockMvc.perform(delete("/employees/delete/email/kalle@anka.se")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$").value("Successfully deleted Employee with Id 1 and email kalle@anka.se"));

            
        when(employeeService.delete("kalle@anka.se")).thenThrow(new EntityNotFoundException("No Employee with email kalle@anka.se found"));

        mockMvc.perform(delete("/employees/delete/email/kalle@anka.se")
                .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.detailedMessage").value("No Employee with email kalle@anka.se found"));
    }
}
