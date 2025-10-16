package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;

import java.util.List;

public class FilterTaskUseCase {

    private final ITasksRepository taskRepository;

    public FilterTaskUseCase(ITasksRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    public List<TaskOutput> execute(TaskStatusEnum status, TaskPriorityEnum priority, Long projectId) {
        return taskRepository.findWithFilter(status, priority, projectId);
    }
}
