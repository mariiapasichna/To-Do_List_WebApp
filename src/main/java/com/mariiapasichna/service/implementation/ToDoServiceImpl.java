package com.mariiapasichna.service.implementation;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.ToDo;
import com.mariiapasichna.repository.ToDoRepository;
import com.mariiapasichna.service.ToDoService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class ToDoServiceImpl implements ToDoService {

    private ToDoRepository todoRepository;

    public ToDoServiceImpl(ToDoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    @Override
    public ToDo create(ToDo todo) {
        try {
            return todoRepository.save(todo);
        } catch (RuntimeException e) {
            throw new NullEntityReferenceException("To-Do cannot be 'null'");
        }
    }

    @Override
    public ToDo readById(long id) {
        return todoRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("To-Do with id " + id + " not found"));
    }

    @Override
    public ToDo update(ToDo todo) {
        if (todo == null) {
            throw new NullEntityReferenceException("To-Do can't be null");
        }
        readById(todo.getId());
        return todoRepository.save(todo);
    }

    @Override
    public void delete(long id) {
        ToDo todo = readById(id);
        todoRepository.delete(todo);
    }

    @Override
    public List<ToDo> getAll() {
        List<ToDo> todos = todoRepository.findAll();
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }

    @Override
    public List<ToDo> getByUserId(long userId) {
        List<ToDo> todos = todoRepository.findAllByOwnerId(userId);
        return todos.isEmpty() ? new ArrayList<>() : todos;
    }
}