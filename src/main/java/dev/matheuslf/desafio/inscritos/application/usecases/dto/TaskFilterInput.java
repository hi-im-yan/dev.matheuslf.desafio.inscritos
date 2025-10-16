package dev.matheuslf.desafio.inscritos.application.usecases.dto;

import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;

public record TaskFilterInput(
    TaskStatusEnum status,
    TaskPriorityEnum priority,
    Long projectId
) {
    public boolean hasStatus() {
        return status != null;
    }
    
    public boolean hasPriority() {
        return priority != null;
    }
    
    public boolean hasProjectId() {
        return projectId != null;
    }
}
