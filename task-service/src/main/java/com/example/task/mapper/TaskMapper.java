package com.example.task.mapper;

import com.example.task.dto.TaskDTO;
import com.example.task.entity.Task;
import org.springframework.stereotype.Component;

@Component
public class TaskMapper {

    public TaskDTO mapToDTO(Task task) {
        return new TaskDTO(task.getTitle(), task.getDescription(), task.getCreatedById(), task.getAssignedTo(), task.getStatus());
    }

    public Task mapToEntity(TaskDTO taskDTO) {
        return new Task(taskDTO.getTitle(), taskDTO.getDescription(), taskDTO.getCreatedById(), taskDTO.getAssignedTo(), taskDTO.getStatus());
    }
}
