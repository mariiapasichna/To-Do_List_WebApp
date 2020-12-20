package com.mariiapasichna.service.implementation;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.State;
import com.mariiapasichna.repository.StateRepository;
import com.mariiapasichna.service.StateService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class StateServiceImpl implements StateService {

    private StateRepository stateRepository;

    public StateServiceImpl(StateRepository stateRepository) {
        this.stateRepository = stateRepository;
    }

    @Override
    public State create(State state) {
        try {
            return stateRepository.save(state);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("State can't be 'null'");
        }
    }

    @Override
    public State readById(long id) {
        return stateRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("State with id " + id + " not found"));
    }

    @Override
    public State update(State state) {
        if (state == null) {
            throw new NullEntityReferenceException("State can't be null");
        }
        readById(state.getId());
        return stateRepository.save(state);
    }

    @Override
    public void delete(long id) {
        State state = readById(id);
        stateRepository.delete(state);
    }

    @Override
    public State getByName(String name) {
        return stateRepository.getByName(name).orElseThrow(() -> new EntityNotFoundException("State with name " + name + " not found"));
    }

    @Override
    public List<State> getAll() {
        List<State> states = stateRepository.findAll();
        return states.isEmpty() ? new ArrayList<>() : states;
    }
}