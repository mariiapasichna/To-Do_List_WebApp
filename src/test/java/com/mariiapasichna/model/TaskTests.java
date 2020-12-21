package com.mariiapasichna.model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Set;

@SpringBootTest
public class TaskTests {
    private static Validator validator;
    private static Task validTask;

    @BeforeAll
    static void init() {
        validTask = new Task();
        validTask.setId(1);
        validTask.setName("TestTask");
        validTask.setPriority(Priority.HIGH);
        validTask.setState(new State());
        validTask.setTodo(new ToDo());

        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    void createValidTask() {
        Set<ConstraintViolation<Task>> violations = validator.validate(validTask);
        Assertions.assertEquals(0, violations.size());
    }

    @Test
    void constraintViolationEmptyTaskName() {
        Task task = new Task();
        task.setName("");
        Set<ConstraintViolation<Task>> violations = validator.validate(task);
        Assertions.assertEquals(1, violations.size());
    }

    @Test
    void constraintViolationNullTaskName() {
        Set<ConstraintViolation<Task>> violations = validator.validate(new Task());
        Assertions.assertEquals(2, violations.size());
    }

    @Test
    public void toStringUserTest() {
        String expected = "Task{" +
                "id=" + validTask.getId() +
                ", name='" + validTask.getName() + '\'' +
                ", priority=" + validTask.getPriority() +
                ", todo=" + validTask.getTodo() +
                ", state=" + validTask.getState() +
                '}';
        Assertions.assertEquals(expected, validTask.toString());
    }
}