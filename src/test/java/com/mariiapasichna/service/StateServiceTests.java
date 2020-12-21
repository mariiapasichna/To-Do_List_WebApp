package com.mariiapasichna.service;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.State;
import com.mariiapasichna.repository.StateRepository;
import com.mariiapasichna.service.implementation.StateServiceImpl;
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
public class StateServiceTests {
    private StateService stateService;
    private State state;

    @MockBean
    private StateRepository stateRepository;

    @BeforeEach
    public void setUp() {
        stateService = new StateServiceImpl(stateRepository);
        state = new State();
        state.setName("TestState");

        Mockito.when(stateRepository.save(state)).thenReturn(state);
        Mockito.when(stateRepository.save(null)).thenThrow(IllegalArgumentException.class);
        Mockito.when(stateRepository.findById(state.getId())).thenReturn(Optional.ofNullable(state));
        Mockito.when(stateRepository.findById(100L)).thenThrow(EntityNotFoundException.class);
        Mockito.doNothing().when(stateRepository).delete(state);
        Mockito.when(stateRepository.findAll()).thenReturn(Arrays.asList(state));
        Mockito.when(stateRepository.getByName("TestState")).thenReturn(Optional.ofNullable(state));
        Mockito.when(stateRepository.getByName("ExceptionState")).thenThrow(EntityNotFoundException.class);
    }

    @Test
    public void createStateTest() {
        Assertions.assertEquals(stateService.create(state), state);
        Assertions.assertThrows(NullEntityReferenceException.class, () -> stateService.create(null));
        Mockito.verify(stateRepository, Mockito.times(1)).save(state);
    }

    @Test
    public void readByIdStateTest() {
        Assertions.assertEquals(stateService.readById(state.getId()), state);
        Assertions.assertThrows(EntityNotFoundException.class, () -> stateService.readById(100L));
        Mockito.verify(stateRepository, Mockito.times(1)).findById(state.getId());
    }

    @Test
    public void updateStateTest() {
        State newState = new State();
        newState.setId(100L);
        Assertions.assertEquals(stateService.update(state), state);
        Assertions.assertThrows(EntityNotFoundException.class, () -> stateService.update(newState));
        Assertions.assertThrows(NullEntityReferenceException.class, () -> stateService.update(null));
        Mockito.verify(stateRepository, Mockito.times(1)).findById(state.getId());
        Mockito.verify(stateRepository, Mockito.times(1)).save(state);
    }

    @Test
    public void deleteStateTest() {
        stateService.delete(state.getId());
        Assertions.assertThrows(EntityNotFoundException.class, () -> stateService.delete(100L));
        Mockito.verify(stateRepository, Mockito.times(1)).findById(state.getId());
        Mockito.verify(stateRepository, Mockito.times(1)).delete(state);
    }

    @Test
    public void getAllStateTest() {
        Assertions.assertEquals(stateService.getAll(), Arrays.asList(state));
        Mockito.when(stateRepository.findAll()).thenReturn(new ArrayList<>());
        Assertions.assertEquals(stateService.getAll(), new ArrayList<>());
        Mockito.verify(stateRepository, Mockito.times(2)).findAll();
    }

    @Test
    public void getByNameStateTest() {
        Assertions.assertEquals(stateService.getByName("TestState"), state);
        Assertions.assertThrows(EntityNotFoundException.class, () -> stateService.getByName("ExceptionState"));
        Mockito.verify(stateRepository, Mockito.times(1)).getByName("ExceptionState");
        Mockito.verify(stateRepository, Mockito.times(1)).getByName("TestState");
    }
}