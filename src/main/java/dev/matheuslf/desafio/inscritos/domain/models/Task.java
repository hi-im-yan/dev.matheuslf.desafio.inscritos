package dev.matheuslf.desafio.inscritos.domain.models;

import dev.matheuslf.desafio.inscritos.domain.exceptions.NotValidException;

import java.time.LocalDate;

public record Task(String title, String description, TaskStatusEnum status, TaskPriorityEnum priority,
                   LocalDate dueDate, Long projectId) {
    public Task(String title, String description, TaskStatusEnum status, TaskPriorityEnum priority, LocalDate dueDate, Long projectId) {
        if (!isTitleValid(title)) {
            throw new NotValidException("Title must be between 5 and 150 characters");
        }
        if (status == null) {
            throw new NotValidException("Status is required");
        }
        if (priority == null) {
            throw new NotValidException("Priority is required");
        }
        if (projectId == null) {
            throw new NotValidException("Project is required");
        }

        this.title = title.trim();
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.projectId = projectId;
    }

    private boolean isTitleValid(String title) {
        return title != null && title.trim().length() >= 5 && title.trim().length() <= 150;
    }
}
