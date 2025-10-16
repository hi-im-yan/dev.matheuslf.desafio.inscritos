package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;

public class DeleteTaskUseCase {

    public DeleteTaskUseCase(ITasksRepository tasksRepository) {
        this.tasksRepository = tasksRepository;
    }

    private final ITasksRepository tasksRepository;
    
    public void execute(Long taskId) {
        if (tasksRepository.findById(taskId) == null) {
            throw new NotFoundException("Task not found with id: " + taskId);
        }
        
        tasksRepository.deleteById(taskId);
    }
}
