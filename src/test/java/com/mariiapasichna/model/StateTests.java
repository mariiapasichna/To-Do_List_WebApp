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
public class StateTests {
    private static Validator validator;
    private static State validState;

    @BeforeAll
    static void init() {
        validState = new State();
        validState.setId(1);
        validState.setName("TestState");
        validState.setTasks(new ArrayList<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createValidState() {
        Set<ConstraintViolation<State>> violations = validator.validate(validState);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void constraintViolationEmptyStateName() {
        State state = new State();
        state.setName("");
        Set<ConstraintViolation<State>> violations = validator.validate(state);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void constraintViolationNullStateName() {
        Set<ConstraintViolation<State>> violations = validator.validate(new State());
        Assertions.assertEquals(2, violations.size());
    }

    @Test
    public void toStringUserTest() {
        String expected = "State{" +
                "id=" + validState.getId() +
                ", name='" + validState.getName() + '\'' +
                '}';
        Assertions.assertEquals(expected, validState.toString());
    }
}