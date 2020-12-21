package com.mariiapasichna.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.ArrayList;
import java.util.Set;
import java.util.stream.Stream;

@SpringBootTest
public class UserTests {
    private static Validator validator;
    private static User validUser;

    @BeforeAll
    static void init() {
        Role traineeRole = new Role();
        traineeRole.setName("TRAINEE");
        validUser = new User();
        validUser.setId(1);
        validUser.setFirstName("Valid-Name");
        validUser.setLastName("Valid-Name");
        validUser.setEmail("test@test.com");
        validUser.setPassword("qwQW12!@");
        validUser.setRole(traineeRole);
        validUser.setMyToDo(new ArrayList<>());
        validUser.setOtherToDo(new ArrayList<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createValidUser() {
        Set<ConstraintViolation<User>> violations = validator.validate(validUser);
        Assertions.assertEquals(0, violations.size());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidEmailUser")
    void constraintViolationInvalidEmail(String input, String errorValue) {
        User user = new User();
        user.setEmail(input);
        user.setFirstName(validUser.getFirstName());
        user.setLastName(validUser.getLastName());
        user.setPassword(validUser.getPassword());
        user.setRole(validUser.getRole());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidEmailUser() {
        return Stream.of(
                Arguments.of("invalidEmail", "invalidEmail"),
                Arguments.of("email@", "email@"),
                Arguments.of("", ""),
                Arguments.of("invalid", "invalid")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNameUser")
    void constraintViolationInvalidFirstName(String input, String errorValue) {
        User user = new User();
        user.setEmail(validUser.getEmail());
        user.setFirstName(input);
        user.setLastName(validUser.getLastName());
        user.setPassword(validUser.getPassword());
        user.setRole(validUser.getRole());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    @ParameterizedTest
    @MethodSource("provideInvalidNameUser")
    void constraintViolationInvalidLastName(String input, String errorValue) {
        User user = new User();
        user.setEmail(validUser.getEmail());
        user.setFirstName(validUser.getFirstName());
        user.setLastName(input);
        user.setPassword(validUser.getPassword());
        user.setRole(validUser.getRole());
        user.setMyToDo(validUser.getMyToDo());
        user.setOtherToDo(validUser.getOtherToDo());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidNameUser() {
        return Stream.of(
                Arguments.of("invalid", "invalid"),
                Arguments.of("Invalid-", "Invalid-"),
                Arguments.of("Invalid-invalid", "Invalid-invalid")
        );
    }

    @ParameterizedTest
    @MethodSource("provideInvalidPasswordUser")
    void constraintViolationInvalidPassword(String input, String errorValue) {
        User user = new User();
        user.setId(validUser.getId());
        user.setFirstName(validUser.getFirstName());
        user.setLastName(validUser.getLastName());
        user.setEmail(validUser.getEmail());
        user.setPassword(input);
        user.setRole(validUser.getRole());

        Set<ConstraintViolation<User>> violations = validator.validate(user);
        Assertions.assertEquals(1, violations.size());
        Assertions.assertEquals(errorValue, violations.iterator().next().getInvalidValue());
    }

    private static Stream<Arguments> provideInvalidPasswordUser() {
        return Stream.of(
                Arguments.of("password", "password"),
                Arguments.of("password1", "password1"),
                Arguments.of("Password", "Password"),
                Arguments.of("Pass1", "Pass1"),
                Arguments.of("PASSWORD", "PASSWORD"),
                Arguments.of("PASSWORD1", "PASSWORD1"),
                Arguments.of("", ""),
                Arguments.of("1111111", "1111111"),
                Arguments.of(null, null)
        );
    }

    @Test
    public void toStringUserTest() {
        String expected = "User{" +
                "id=" + validUser.getId() +
                ", firstName='" + validUser.getFirstName() + '\'' +
                ", lastName='" + validUser.getLastName() + '\'' +
                ", email='" + validUser.getEmail() + '\'' +
                ", password='" + validUser.getPassword() + '\'' +
                ", role=" + validUser.getRole() +
                '}';
        Assertions.assertEquals(expected, validUser.toString());
    }
}