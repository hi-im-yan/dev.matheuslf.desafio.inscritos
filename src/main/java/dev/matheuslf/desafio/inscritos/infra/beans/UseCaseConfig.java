package dev.matheuslf.desafio.inscritos.infra.beans;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.CreateProjectUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.CreateTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.DeleteTaskUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.ListProjectsUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.FilterTaskUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final IProjectRepository projectRepository;
    private final ITasksRepository taskRepository;

    public UseCaseConfig(IProjectRepository projectRepository, ITasksRepository tasksRepository) {
        this.projectRepository = projectRepository;
        this.taskRepository = tasksRepository;
    }

    @Bean
    public CreateProjectUseCase createProjectUseCase() {
        return new CreateProjectUseCase(projectRepository);
    }

    @Bean
    public ListProjectsUseCase listProjectsUseCase() {
        return new ListProjectsUseCase(projectRepository);
    }

    @Bean
    public CreateTaskUseCase createTaskUseCase() {
        return new CreateTaskUseCase(taskRepository, projectRepository);
    }

    @Bean
    public FilterTaskUseCase listTasksUseCase() {
        return new FilterTaskUseCase(taskRepository);
    }

    @Bean
    public DeleteTaskUseCase deleteTaskUseCase() {
        return new DeleteTaskUseCase(taskRepository);
    }
}
