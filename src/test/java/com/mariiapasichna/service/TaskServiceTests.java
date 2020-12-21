package com.mariiapasichna.service;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.Task;
import com.mariiapasichna.repository.TaskRepository;
import com.mariiapasichna.service.implementation.TaskServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class TaskServiceTests {
    private TaskService taskService;
    private Task task;

    @MockBean
    private TaskRepository taskRepository;

    @BeforeEach
    public void setUp() {
        taskService = new TaskServiceImpl(taskRepository);
        task = new Task();
        task.setName("TestTask");

        Mockito.when(taskRepository.save(task)).thenReturn(task);
        Mockito.when(taskRepository.save(null)).thenThrow(IllegalArgumentException.class);
        Mockito.when(taskRepository.findById(task.getId())).thenReturn(Optional.ofNullable(task));
        Mockito.when(taskRepository.findById(100L)).thenThrow(EntityNotFoundException.class);
        Mockito.doNothing().when(taskRepository).delete(task);
        Mockito.when(taskRepository.findAll()).thenReturn(Arrays.asList(task));
        Mockito.when(taskRepository.getByTodoId(1L)).thenReturn(Arrays.asList(task));
        Mockito.when(taskRepository.getByTodoId(100L)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void createTaskTest() {
        Assertions.assertEquals(taskService.create(task), task);
        Assertions.assertThrows(NullEntityReferenceException.class, () -> taskService.create(null));
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    public void readByIdTaskTest() {
        Assertions.assertEquals(taskService.readById(task.getId()), task);
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.readById(100L));
        Mockito.verify(taskRepository, Mockito.times(1)).findById(task.getId());
    }

    @Test
    public void updateTaskTest() {
        Task newTask = new Task();
        newTask.setId(100L);
        Assertions.assertEquals(taskService.update(task), task);
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.update(newTask));
        Assertions.assertThrows(NullEntityReferenceException.class, () -> taskService.update(null));
        Mockito.verify(taskRepository, Mockito.times(1)).findById(task.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).save(task);
    }

    @Test
    public void deleteTaskTest() {
        taskService.delete(task.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.delete(100L));
        Mockito.verify(taskRepository, Mockito.times(1)).findById(task.getId());
        Mockito.verify(taskRepository, Mockito.times(1)).delete(task);
    }

    @Test
    public void getAllTaskTest() {
        Assertions.assertEquals(taskService.getAll(), Arrays.asList(task));
        Mockito.when(taskRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(taskService.getAll(), new ArrayList<>());
        Mockito.verify(taskRepository, Mockito.times(2)).findAll();
    }

    @Test
    public void getByTodoIdTaskTest() {
        Assertions.assertEquals(taskService.getByTodoId(1L), Arrays.asList(task));
        Assertions.assertThrows(EntityNotFoundException.class, () -> taskService.getByTodoId(100L));
        Mockito.verify(taskRepository, Mockito.times(1)).getByTodoId(1L);
    }
}