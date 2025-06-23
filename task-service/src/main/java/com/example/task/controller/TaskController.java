package com.example.task.controller;

import com.example.task.dto.TaskDTO;
import com.example.task.entity.Task;
import com.example.task.entity.TaskStatus;
import com.example.task.kafka.UserCheckKafkaClient;
import com.example.task.service.TaskService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
public class TaskController {

    @Autowired
    private UserCheckKafkaClient userCheckKafkaClient;

    private final TaskService taskService;


    @GetMapping("/check-user/{userId}")
    public  ResponseEntity<String> checkUser(@PathVariable Long userId) throws JsonProcessingException {
        try{
            boolean exists = userCheckKafkaClient.checkUserExists(userId);
            return ResponseEntity.ok(exists ? "YES" : "NO");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.REQUEST_TIMEOUT).body("Error: " + e.getMessage());
        }
    }

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDTO> getAllTasks() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Task> getTaskById(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(task);
    }

    @GetMapping("/user/{userId}")
    public List<Task> getTasksByUserId(@PathVariable Long userId) {
        return taskService.findByUserId(userId);
    }

    @PostMapping
    public TaskDTO createTask(@RequestBody TaskDTO task) {
        taskService.save(task);
        return task;
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Task> updateTaskStatus(@PathVariable Long id, @RequestBody TaskStatus status) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        taskService.changeTaskStatus(id, status);
        return ResponseEntity.ok(taskService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTask(@PathVariable Long id) {
        Task task = taskService.findById(id);
        if (task == null) {
            return ResponseEntity.notFound().build();
        }
        taskService.delete(task);
        return ResponseEntity.noContent().build();
    }
}
