package com.mariiapasichna.repository;

import com.mariiapasichna.model.Task;
import com.mariiapasichna.model.ToDo;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class TaskRepositoryTests {
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private ToDoRepository toDoRepository;
    @Autowired
    private TestEntityManager entityManager;

    @Test
    public void getByTodoIdTaskTest() {
        ToDo toDo = new ToDo();
        toDo.setTitle("TestToDo");
        toDo.setCreatedAt(LocalDateTime.now());
        ToDo testToDo = entityManager.persist(toDo);
        Task task = new Task();
        task.setName("TestTask");
        task.setTodo(testToDo);
        testToDo.setTasks(Arrays.asList(entityManager.persist(task)));
        List<Task> expected = toDoRepository.findById(testToDo.getId()).get().getTasks();
        List<Task> actual = taskRepository.getByTodoId(testToDo.getId());
        entityManager.flush();

        Assertions.assertEquals(expected, actual);
        Assertions.assertEquals(new ArrayList<>(), taskRepository.getByTodoId(100));
    }
}