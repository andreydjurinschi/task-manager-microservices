package com.example.task.service;

import com.example.task.entity.Task;
import com.example.task.entity.TaskStatus;
import com.example.task.repository.TaskRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<Task> findAll() {
        return taskRepository.findAll();
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public Task save(Task task) {
        return taskRepository.save(task);
    }

    public void changeTaskStatus(Long taskId, TaskStatus status) {
        taskRepository.updateTaskStatus(taskId, status);
    }


    public List<Task> findByUserId(Long userId) {
        return taskRepository.findTaskByassignedTo(userId);
    }

    public void delete(Task task) {
        taskRepository.delete(task);
    }
}
