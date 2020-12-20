package com.mariiapasichna.controller;

import com.mariiapasichna.dto.TaskDto;
import com.mariiapasichna.dto.TaskTransformer;
import com.mariiapasichna.model.Priority;
import com.mariiapasichna.model.Task;
import com.mariiapasichna.service.StateService;
import com.mariiapasichna.service.TaskService;
import com.mariiapasichna.service.ToDoService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/tasks")
public class TaskController {

    private final TaskService taskService;
    private final ToDoService todoService;
    private final StateService stateService;

    public TaskController(TaskService taskService, ToDoService todoService, StateService stateService) {
        this.taskService = taskService;
        this.todoService = todoService;
        this.stateService = stateService;
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @toDoController.canReadToDo(#todoId)")
    @GetMapping("/create/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model) {
        model.addAttribute("task", new TaskDto());
        model.addAttribute("todo", todoService.readById(todoId));
        model.addAttribute("priorities", Priority.values());
        return "create-task";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @toDoController.canReadToDo(#todoId)")
    @PostMapping("/create/{todo_id}")
    public String create(@PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDto taskDto, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("todo", todoService.readById(todoId));
            model.addAttribute("priorities", Priority.values());
            return "create-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(taskDto.getTodoId()),
                stateService.getByName("New"));
        taskService.create(task);
        return "redirect:/todos/" + todoId + "/read";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @toDoController.canReadToDo(#todoId)")
    @GetMapping("/{task_id}/update/{todo_id}")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model) {
        TaskDto taskDto = TaskTransformer.convertToDto(taskService.readById(taskId));
        model.addAttribute("task", taskDto);
        model.addAttribute("priorities", Priority.values());
        model.addAttribute("states", stateService.getAll());
        return "update-task";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @toDoController.canReadToDo(#todoId)")
    @PostMapping("/{task_id}/update/{todo_id}")
    public String update(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId, Model model,
                         @Validated @ModelAttribute("task") TaskDto taskDto, BindingResult result) {
        if (result.hasErrors()) {
            model.addAttribute("priorities", Priority.values());
            model.addAttribute("states", stateService.getAll());
            return "update-task";
        }
        Task task = TaskTransformer.convertToEntity(
                taskDto,
                todoService.readById(taskDto.getTodoId()),
                stateService.readById(taskDto.getStateId()));
        taskService.update(task);
        return "redirect:/todos/" + todoId + "/read";
    }

    @PreAuthorize("hasAuthority('ADMIN') or hasAuthority('USER') and @toDoController.canReadToDo(#todoId)")
    @GetMapping("/{task_id}/delete/{todo_id}")
    public String delete(@PathVariable("task_id") long taskId, @PathVariable("todo_id") long todoId) {
        taskService.delete(taskId);
        return "redirect:/todos/" + todoId + "/read";
    }
}