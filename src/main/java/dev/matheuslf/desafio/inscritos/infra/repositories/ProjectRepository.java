package dev.matheuslf.desafio.inscritos.infra.repositories;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.infra.mappers.ProjectMapper;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.ProjectJpaRepository;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class ProjectRepository implements IProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;
    
    public ProjectRepository(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }
    
    @Override
    public ProjectOutput save(Project project) {
        ProjectEntity entity = ProjectMapper.toEntity(project);
        ProjectEntity savedEntity = projectJpaRepository.save(entity);
        return ProjectMapper.toOutput(savedEntity);
    }

    @Override
    public List<ProjectOutput> findAll() {
        return projectJpaRepository.findAll().stream()
                .map(ProjectMapper::toOutput)
                .collect(Collectors.toList());
    }

    @Override
    public ProjectOutput findByName(String projectName) {
        return projectJpaRepository.findByName(projectName)
                .map(ProjectMapper::toOutput)
                .orElse(null);
    }

    @Override
    public Optional<Project> findById(Long id) {
        ProjectEntity entity = projectJpaRepository.findById(id).orElse(null);
        if (entity == null) {
            return Optional.empty();
        }
        return Optional.of(ProjectMapper.toDomain(entity));
    }

}
