package dev.matheuslf.desafio.inscritos.infra.repositories;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.ProjectJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class ProjectRepository implements IProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;
    
    public ProjectRepository(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }
    
    @Override
    public ProjectOutput save(Project project) {
        ProjectEntity entity = toEntity(project);
        ProjectEntity savedEntity = projectJpaRepository.save(entity);
        return toProjectOutput(savedEntity);
    }

    @Override
    public List<ProjectOutput> findAll() {
        return projectJpaRepository.findAll().stream()
                .map(this::toProjectOutput)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectOutput findByName(String projectName) {
        return projectJpaRepository.findByName(projectName)
                .map(this::toProjectOutput)
                .orElse(null);
    }

    private ProjectEntity toEntity(Project project) {
        return ProjectEntity.builder()
                .id(null) // Let JPA handle ID generation
                .name(project.name())
                .description(project.description())
                .startDate(project.startDate())
                .endDate(project.endDate())
                .build();
    }

    private ProjectOutput toProjectOutput(ProjectEntity entity) {
        return new ProjectOutput(
                entity.getName(),
                entity.getDescription(),
                entity.getStartDate(),
                entity.getEndDate()
        );
    }
}
