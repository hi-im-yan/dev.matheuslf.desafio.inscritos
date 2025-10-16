package dev.matheuslf.desafio.inscritos.infra.mappers;

import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateTaskInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.domain.models.Task;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.TaskEntity;

/**
 * Mapper for converting between Task domain model, entity and DTOs.
 */
public class TaskMapper {

    /**
     * Converts a TaskEntity to a Task domain model.
     *
     * @param entity the TaskEntity to convert
     * @return the converted Task domain model
     */
    public static Task toDomain(TaskEntity entity) {
        if (entity == null) {
            return null;
        }
        
        Project project = new Project(
            entity.getProject().getName(),
            entity.getProject().getDescription(),
            entity.getProject().getStartDate(),
            entity.getProject().getEndDate()
        );
        
        return new Task(
            entity.getTitle(),
            entity.getDescription(),
            entity.getStatus(),
            entity.getPriority(),
            entity.getDueDate(),
            entity.getProject().getId()
        );
    }

    /**
     * Converts a Task domain model to a TaskEntity.
     *
     * @param task the Task domain model to convert
     * @return the converted TaskEntity
     */
    public static TaskEntity toEntity(Task task, ProjectEntity projectEntity) {
        TaskEntity entity = new TaskEntity();
        entity.setTitle(task.title());
        entity.setDescription(task.description());
        entity.setStatus(task.status());
        entity.setPriority(task.priority());
        entity.setDueDate(task.dueDate());
        entity.setProject(projectEntity);
        
        return entity;
    }

    /**
     * Converts a TaskInput DTO to a TaskEntity.
     *
     * @param input the TaskInput to convert
     * @return the converted TaskEntity
     */
    public static TaskEntity toEntity(CreateTaskInput input) {
        if (input == null) {
            return null;
        }
        
        TaskEntity entity = new TaskEntity();
        entity.setTitle(input.title());
        entity.setDescription(input.description());
        entity.setStatus(input.status() != null ? input.status() : TaskStatusEnum.TODO);
        entity.setPriority(input.priority() != null ? input.priority() : TaskPriorityEnum.MEDIUM);
        entity.setDueDate(input.dueDate());
        
        // Project will be set by the service layer
        
        return entity;
    }

    /**
     * Converts a TaskEntity to a TaskOutput DTO.
     *
     * @param entity the TaskEntity to convert
     * @return the converted TaskOutput
     */
    public static TaskOutput toOutput(TaskEntity entity) {
        return new TaskOutput(
            entity.getId(),
            entity.getTitle(),
            entity.getDescription(),
            entity.getStatus(),
            entity.getPriority(),
            entity.getDueDate(),
            entity.getProject().getId()
        );
    }

}
