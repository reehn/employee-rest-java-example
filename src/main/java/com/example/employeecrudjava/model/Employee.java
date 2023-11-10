package com.example.employeecrudjava.model;

import jakarta.annotation.Nonnull;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@Entity
@Table(name = "employees")
public class Employee {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        @Nonnull
        @Pattern(regexp = "^[\\S|\\s|-]{1,30}$", message = "Field firstName is required and must be between 1 and 30 characters long")
        String firstName;

        @Nonnull
        @Pattern(regexp = "^[\\S|\\s|-]{1,30}$", message = "Field lastName is required and must be between 1 and 30 characters long")
        String lastName;

        @Nonnull
        @Column(unique = true)
        @Pattern(regexp = "^[\\w!#$%&'*+/=?`{|}~^-]+(?:\\.[\\w!#$%&'*+/=?`{|}~^-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,6}$", message = "Email is required and must be a valid email-address")
        private String email;
}
