package com.example.task.dto;

import com.example.task.entity.TaskStatus;

public class TaskDTO {
    private String title;
    private String description;
    private Long createdById;
    private Long assignedTo;
    private TaskStatus status;

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public TaskDTO(String title, String description, Long createdById, Long assignedTo, TaskStatus status) {
        this.title = title;
        this.description = description;
        this.createdById = createdById;
        this.assignedTo = assignedTo;
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCreatedById() {
        return createdById;
    }

    public void setCreatedById(Long createdById) {
        this.createdById = createdById;
    }

    public Long getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Long assignedTo) {
        this.assignedTo = assignedTo;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }
}
