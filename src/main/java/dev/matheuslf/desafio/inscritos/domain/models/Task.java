package dev.matheuslf.desafio.inscritos.domain.models;

import dev.matheuslf.desafio.inscritos.domain.exceptions.NotValidException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDate;

@Getter
@EqualsAndHashCode
@ToString
public class Task {
    private String title;
    private String description;
    private TaskStatusEnum status;
    private TaskPriorityEnum priority;
    private LocalDate dueDate;
    private Project project;

    public Task(String title, String description, TaskStatusEnum status, TaskPriorityEnum priority, LocalDate dueDate, Project project) {
        if (!isTitleValid(title)) {
            throw new NotValidException("Title must be between 5 and 150 characters");
        }
        if (status == null) {
            throw new NotValidException("Status is required");
        }
        if (priority == null) {
            throw new NotValidException("Priority is required");
        }
        if (project == null) {
            throw new NotValidException("Project is required");
        }
        if (dueDate != null && dueDate.isBefore(project.getStartDate())) {
            throw new NotValidException("Due date cannot be before the project start date");
        }
        
        this.title = title.trim();
        this.description = description;
        this.status = status;
        this.priority = priority;
        this.dueDate = dueDate;
        this.project = project;
    }

    private boolean isTitleValid(String title) {
        return title != null && title.trim().length() >= 5 && title.trim().length() <= 150;
    }
}
