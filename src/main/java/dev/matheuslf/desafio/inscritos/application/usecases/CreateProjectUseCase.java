package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.AlreadyExistsException;
import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;

public class CreateProjectUseCase {

    private final IProjectRepository projectRepository;

    public CreateProjectUseCase(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public ProjectOutput execute(Project input) {
        ProjectOutput projectOutput = projectRepository.findByName(input.name());
        if (projectOutput != null) {
            throw new AlreadyExistsException("Project already exists");
        }

        return projectRepository.save(input);
    }
}
