package dev.matheuslf.desafio.inscritos.application.interfaces;

import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.domain.models.Task;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;

import java.util.List;

public interface ITasksRepository {

    TaskOutput save(Task task);
    List<TaskOutput> findWithFilter(TaskStatusEnum status, TaskPriorityEnum priority, Long projectId);
    TaskOutput updateStatus(Long taskId, TaskStatusEnum status);
    void deleteById(Long taskId);
    TaskOutput findById(Long taskId);

}
