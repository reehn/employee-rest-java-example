package com.example.employeecrudjava.testUtils;

import com.example.employeecrudjava.model.Employee;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TestUtils {

    public static Employee getTestEmployee() {
        return new Employee(1L, "Kalle", "Anka", "kalle@anka.se");
    }

    public static Employee getTestEmployee(String firstName, String lastName, String email) {
        return new Employee(null, firstName, lastName, email);
    }

    public static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
