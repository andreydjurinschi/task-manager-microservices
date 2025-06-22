package com.example.task.service;

import com.example.task.dto.TaskDTO;
import com.example.task.entity.Task;
import com.example.task.entity.TaskStatus;
import com.example.task.mapper.TaskMapper;
import com.example.task.repository.TaskRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TaskService {

    private final TaskRepository taskRepository;
    private final TaskMapper taskMapper;


    @Autowired
    public TaskService(TaskRepository taskRepository, TaskMapper taskMapper) {
        this.taskRepository = taskRepository;
        this.taskMapper = taskMapper;
    }

    public List<TaskDTO> findAll() {
        List<Task> tasks = taskRepository.findAll();
        List<TaskDTO> dtos = new ArrayList<>();
        for(var task : tasks){
               dtos.add(taskMapper.mapToDTO(task));
        }
        return dtos;
    }

    public Task findById(Long id) {
        return taskRepository.findById(id).orElse(null);
    }

    public void save(TaskDTO taskDTO) {
        Task task = taskMapper.mapToEntity(taskDTO);
        taskRepository.save(task);
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
