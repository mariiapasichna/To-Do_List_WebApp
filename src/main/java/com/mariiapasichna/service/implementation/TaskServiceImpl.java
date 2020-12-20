package com.mariiapasichna.service.implementation;

import com.mariiapasichna.exception.NullEntityReferenceException;
import com.mariiapasichna.model.Task;
import com.mariiapasichna.repository.TaskRepository;
import com.mariiapasichna.service.TaskService;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;

@Service
public class TaskServiceImpl implements TaskService {

    private TaskRepository taskRepository;

    public TaskServiceImpl(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Override
    public Task create(Task user) {
        try {
            return taskRepository.save(user);
        } catch (IllegalArgumentException e) {
            throw new NullEntityReferenceException("Task can't be 'null'");
        }
    }

    @Override
    public Task readById(long id) {
        return taskRepository.findById(id).orElseThrow(() -> new EntityNotFoundException("Task with id " + id + " not found"));
    }

    @Override
    public Task update(Task task) {
        if (task == null) {
            throw new NullEntityReferenceException("Task can't be null");
        }
        readById(task.getId());
        return taskRepository.save(task);
    }

    @Override
    public void delete(long id) {
        Task task = readById(id);
        taskRepository.delete(task);
    }

    @Override
    public List<Task> getAll() {
        List<Task> tasks = taskRepository.findAll();
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }

    @Override
    public List<Task> getByTodoId(long todoId) {
        List<Task> tasks = taskRepository.getByTodoId(todoId);
        return tasks.isEmpty() ? new ArrayList<>() : tasks;
    }
}