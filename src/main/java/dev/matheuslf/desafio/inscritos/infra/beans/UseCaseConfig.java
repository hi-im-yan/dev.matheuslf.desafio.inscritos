package dev.matheuslf.desafio.inscritos.infra.beans;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.CreateProjectUseCase;
import dev.matheuslf.desafio.inscritos.application.usecases.ListProjectsUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCaseConfig {

    private final IProjectRepository projectRepository;

    public UseCaseConfig(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @Bean
    public CreateProjectUseCase createProjectUseCase() {
        return new CreateProjectUseCase(projectRepository);
    }

    @Bean
    public ListProjectsUseCase listProjectsUseCase() {
        return new ListProjectsUseCase(projectRepository);
    }
}
