package com.mariiapasichna.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Set;

@SpringBootTest
public class ToDoTests {
    private static Validator validator;
    private static ToDo validToDo;

    @BeforeAll
    static void init() {
        validToDo = new ToDo();
        validToDo.setId(1);
        validToDo.setTitle("TestToDo");
        validToDo.setCreatedAt(LocalDateTime.now());
        validToDo.setOwner(new User());
        validToDo.setCollaborators(new ArrayList<>());
        validToDo.setTasks(new ArrayList<>());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createValidTodo() {
        Set<ConstraintViolation<ToDo>> violations = validator.validate(validToDo);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void constraintViolationEmptyToDoTitle() {
        ToDo toDo = new ToDo();
        toDo.setTitle("");
        toDo.setCreatedAt(validToDo.getCreatedAt());

        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void constraintViolationNullToDoTitle() {
        ToDo toDo = new ToDo();
        toDo.setCreatedAt(validToDo.getCreatedAt());

        Set<ConstraintViolation<ToDo>> violations = validator.validate(toDo);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    public void toStringUserTest() {
        String expected = "ToDo{" +
                "id=" + validToDo.getId() +
                ", title='" + validToDo.getTitle() + '\'' +
                ", createdAt=" + validToDo.getCreatedAt() +
                '}';
        Assertions.assertEquals(expected, validToDo.toString());
    }
}