package dev.matheuslf.desafio.inscritos.application.interfaces;

import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;

import java.util.List;
import java.util.Optional;

public interface IProjectRepository {

    ProjectOutput save(Project project);
    List<ProjectOutput> findAll();
    ProjectOutput findByName(String projectName);
    Optional<Project> findById(Long id);
}
