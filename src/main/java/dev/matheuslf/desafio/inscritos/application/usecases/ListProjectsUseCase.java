package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;

import java.util.List;

public class ListProjectsUseCase {

    private final IProjectRepository projectRepository;

    public ListProjectsUseCase(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public List<ProjectOutput> execute() {
        return projectRepository.findAll();
    }
}
