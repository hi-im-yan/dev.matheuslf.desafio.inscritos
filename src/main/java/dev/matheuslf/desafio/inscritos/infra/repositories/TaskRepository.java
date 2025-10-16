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
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

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
        ProjectEntity projectEntity = projectJpaRepository.findById(task.projectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));

        TaskEntity entity = TaskMapper.toEntity(task, projectEntity);
        TaskEntity savedEntity = taskJpaRepository.save(entity);
        return TaskMapper.toOutput(savedEntity);
    }
    

    @Override
    public List<TaskOutput> findWithFilter(TaskStatusEnum status, TaskPriorityEnum priority, Long projectId) {
        return taskJpaRepository.findByStatusAndPriorityAndProjectId(status, priority, projectId)
                .stream()
                .map(TaskMapper::toOutput)
                .collect(Collectors.toList());
    }
    

    @Override
    public TaskOutput updateStatus(Long taskId, TaskStatusEnum status) {
        TaskEntity entity = taskJpaRepository.findById(taskId).orElseThrow(() -> new NotFoundException("Task not found"));
        entity.setStatus(status);
        TaskEntity updatedEntity = taskJpaRepository.save(entity);
        return TaskMapper.toOutput(updatedEntity);
    }
    
    @Override
    public void deleteById(Long taskId) {
        taskJpaRepository.deleteById(taskId);
    }
    
    @Override
    public TaskOutput findById(Long taskId) {
        return taskJpaRepository.findById(taskId)
                .map(TaskMapper::toOutput)
                .orElse(null);
    }

}
