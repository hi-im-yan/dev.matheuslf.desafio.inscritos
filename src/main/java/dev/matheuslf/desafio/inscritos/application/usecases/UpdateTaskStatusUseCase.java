package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.UpdateTaskStatusInput;

public class UpdateTaskStatusUseCase {

    private final ITasksRepository tasksRepository;

    public UpdateTaskStatusUseCase(ITasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    public TaskOutput execute(UpdateTaskStatusInput input) {
        TaskOutput existingTask = tasksRepository.findById(input.taskId());

        if (existingTask == null) {
            throw new NotFoundException("Task not found with id: " + input.taskId());
        }

        return tasksRepository.updateStatus(input.taskId(), input.status());
    }
}
