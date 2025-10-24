package dev.matheuslf.desafio.inscritos.infra.repositories;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Task;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.mappers.TaskMapper;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.TaskEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.ProjectJpaRepository;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.TaskJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class TaskRepository implements ITasksRepository {
    
    private final TaskJpaRepository taskJpaRepository;
    private final ProjectJpaRepository projectJpaRepository;
    
    public TaskRepository(TaskJpaRepository taskJpaRepository, ProjectJpaRepository projectJpaRepository) {
        this.taskJpaRepository = taskJpaRepository;
        this.projectJpaRepository = projectJpaRepository;
    }
    
    @Override
    public TaskOutput save(Task task) {
        log.debug("Saving task: title={}, projectId={}", task.title(), task.projectId());
        ProjectEntity projectEntity = projectJpaRepository.findById(task.projectId())
                .orElseThrow(() -> {
                    log.error("Project not found with id: {}", task.projectId());
                    return new NotFoundException("Project not found");
                });

        TaskEntity entity = TaskMapper.toEntity(task, projectEntity);
        TaskEntity savedEntity = taskJpaRepository.save(entity);
        log.debug("Task saved successfully: id={}, title={}, status={}", 
                 savedEntity.getId(), savedEntity.getTitle(), savedEntity.getStatus());
        return TaskMapper.toOutput(savedEntity);
    }
    

    @Override
    public List<TaskOutput> findWithFilter(TaskStatusEnum status, TaskPriorityEnum priority, Long projectId) {
        log.debug("Searching tasks with filters - status: {}, priority: {}, projectId: {}", 
                 status, priority, projectId);
        List<TaskOutput> tasks = taskJpaRepository.findByStatusAndPriorityAndProjectId(status, priority, projectId)
                .stream()
                .map(task -> {
                    log.trace("Found task: id={}, title={}, status={}", 
                            task.getId(), task.getTitle(), task.getStatus());
                    return TaskMapper.toOutput(task);
                })
                .collect(Collectors.toList());
        log.debug("Found {} tasks matching the filters", tasks.size());
        return tasks;
    }
    

    @Override
    public TaskOutput updateStatus(Long taskId, TaskStatusEnum status) {
        log.info("Updating task status - taskId: {}, newStatus: {}", taskId, status);
        TaskEntity entity = taskJpaRepository.findById(taskId)
                .orElseThrow(() -> {
                    log.error("Task not found with id: {}", taskId);
                    return new NotFoundException("Task not found");
                });
        log.debug("Current task status before update - id: {}, currentStatus: {}", 
                 entity.getId(), entity.getStatus());
        entity.setStatus(status);
        TaskEntity updatedEntity = taskJpaRepository.save(entity);
        log.info("Task status updated - id: {}, newStatus: {}", 
                updatedEntity.getId(), updatedEntity.getStatus());
        return TaskMapper.toOutput(updatedEntity);
    }
    
    @Override
    public void deleteById(Long taskId) {
        log.info("Deleting task with id: {}", taskId);
        if (!taskJpaRepository.existsById(taskId)) {
            log.warn("Attempted to delete non-existent task with id: {}", taskId);
            throw new NotFoundException("Task not found");
        }
        taskJpaRepository.deleteById(taskId);
        log.info("Task deleted successfully - id: {}", taskId);
    }
    
    @Override
    public TaskOutput findById(Long taskId) {
        log.debug("Searching for task by id: {}", taskId);
        return taskJpaRepository.findById(taskId)
                .map(task -> {
                    log.debug("Found task: id={}, title={}, status={}", 
                            task.getId(), task.getTitle(), task.getStatus());
                    return TaskMapper.toOutput(task);
                })
                .orElseGet(() -> {
                    log.debug("Task not found with id: {}", taskId);
                    return null;
                });
    }

}
