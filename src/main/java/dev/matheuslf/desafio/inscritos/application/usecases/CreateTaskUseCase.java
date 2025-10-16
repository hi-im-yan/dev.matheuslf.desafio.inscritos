package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateTaskInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.exceptions.NotValidException;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.domain.models.Task;

public class CreateTaskUseCase {

    private final ITasksRepository taskRepository;
    private final IProjectRepository projectRepository;

    public CreateTaskUseCase(ITasksRepository taskRepository, IProjectRepository projectRepository) {
        this.taskRepository = taskRepository;
        this.projectRepository = projectRepository;
    }

    public TaskOutput execute(CreateTaskInput input) {
        Project project = projectRepository.findById(input.projectId())
                .orElseThrow(() -> new NotFoundException("Project not found"));

        if (input.dueDate().isBefore(project.startDate())){
            throw new NotValidException("Task due date cannot be before the project start date");
        }

        Task task = new Task(input.title(), input.description(), input.status(), input.priority(), input.dueDate(), input.projectId());

        return taskRepository.save(task);
    }

}
