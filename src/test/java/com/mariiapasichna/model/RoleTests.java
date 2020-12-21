package com.mariiapasichna.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;

@SpringBootTest
public class RoleTests {
    private static Validator validator;
    private static Role validRole;

    @BeforeAll
    static void init() {
        validRole = new Role();
        validRole.setId(1);
        validRole.setName("TestRole");
        validRole.setUsers(new ArrayList<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createValidRole() {
        Set<ConstraintViolation<Role>> violations = validator.validate(validRole);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void constraintViolationEmptyRoleName() {
        Role role = new Role();
        role.setName("");

        Set<ConstraintViolation<Role>> violations = validator.validate(role);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void constraintViolationNullRoleName() {
        Set<ConstraintViolation<Role>> violations = validator.validate(new Role());
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void toStringUserTest() {
        String expected = "Role{" +
                "id=" + validRole.getId() +
                ", name='" + validRole.getName() + '\'' +
                '}';
        Assertions.assertEquals(expected, validRole.toString());
    }
}