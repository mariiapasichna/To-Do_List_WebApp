package com.mariiapasichna.service;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.ToDo;
import com.mariiapasichna.repository.ToDoRepository;
import com.mariiapasichna.service.implementation.ToDoServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
public class ToDoServiceTests {
    private ToDoService toDoService;
    private ToDo toDo;

    @MockBean
    ToDoRepository toDoRepository;

    @BeforeEach
    public void setUp() {
        toDoService = new ToDoServiceImpl(toDoRepository);
        toDo = new ToDo();
        toDo.setTitle("TestToDo");
        toDo.setCreatedAt(LocalDateTime.now());

        Mockito.when(toDoRepository.save(toDo)).thenReturn(toDo);
        Mockito.when(toDoRepository.save(null)).thenThrow(IllegalArgumentException.class);
        Mockito.when(toDoRepository.findById(toDo.getId())).thenReturn(Optional.ofNullable(toDo));
        Mockito.when(toDoRepository.findById(100L)).thenThrow(EntityNotFoundException.class);
        Mockito.doNothing().when(toDoRepository).delete(toDo);
        Mockito.when(toDoRepository.findAll()).thenReturn(Arrays.asList(toDo));
        Mockito.when(toDoRepository.findAllByOwnerId(1L)).thenReturn(Arrays.asList(toDo));
        Mockito.when(toDoRepository.findAllByOwnerId(100L)).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void createToDoTest() {
        Assertions.assertEquals(toDoService.create(toDo), toDo);
        Assertions.assertThrows(NullEntityReferenceException.class, () -> toDoService.create(null));
        Mockito.verify(toDoRepository, Mockito.times(1)).save(toDo);
    }

    @Test
    public void readByIdToDoTest() {
        Assertions.assertEquals(toDoService.readById(toDo.getId()), toDo);
        Assertions.assertThrows(EntityNotFoundException.class, () -> toDoService.readById(100L));
        Mockito.verify(toDoRepository, Mockito.times(1)).findById(toDo.getId());
    }

    @Test
    public void updateToDoTest() {
        ToDo newToDo = new ToDo();
        newToDo.setId(100L);
        Assertions.assertEquals(toDoService.update(toDo), toDo);
        Assertions.assertThrows(EntityNotFoundException.class, () -> toDoService.update(newToDo));
        Assertions.assertThrows(NullEntityReferenceException.class, () -> toDoService.update(null));
        Mockito.verify(toDoRepository, Mockito.times(1)).findById(toDo.getId());
        Mockito.verify(toDoRepository, Mockito.times(1)).save(toDo);
    }

    @Test
    public void deleteToDoTest() {
        toDoService.delete(toDo.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> toDoService.delete(100L));
        Mockito.verify(toDoRepository, Mockito.times(1)).findById(toDo.getId());
        Mockito.verify(toDoRepository, Mockito.times(1)).findById(100L);
        Mockito.verify(toDoRepository, Mockito.times(1)).delete(toDo);
    }

    @Test
    public void getAllToDoTest() {
        Assertions.assertEquals(toDoService.getAll(), Arrays.asList(toDo));
        Mockito.when(toDoRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(toDoService.getAll(), new ArrayList<>());
        Mockito.verify(toDoRepository, Mockito.times(2)).findAll();
    }

    @Test
    public void getByUserIdToDoTest() {
        Assertions.assertEquals(toDoService.getByUserId(1L), Arrays.asList(toDo));
        Assertions.assertThrows(EntityNotFoundException.class, () -> toDoService.getByUserId(100L));
        Mockito.when(toDoRepository.findAllByOwnerId(1L)).thenReturn(new ArrayList<>());
        Assertions.assertEquals(toDoService.getByUserId(1L), new ArrayList<>());
        Mockito.verify(toDoRepository, Mockito.times(2)).findAllByOwnerId(1L);
    }
}