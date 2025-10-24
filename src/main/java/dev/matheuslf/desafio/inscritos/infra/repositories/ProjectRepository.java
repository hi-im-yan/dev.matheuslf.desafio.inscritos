package dev.matheuslf.desafio.inscritos.infra.repositories;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.infra.mappers.ProjectMapper;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.ProjectJpaRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ProjectRepository implements IProjectRepository {
    private final ProjectJpaRepository projectJpaRepository;
    
    public ProjectRepository(ProjectJpaRepository projectJpaRepository) {
        this.projectJpaRepository = projectJpaRepository;
    }
    
    @Override
    public ProjectOutput save(Project project) {
        log.debug("Saving project: name={}", project.name());
        ProjectEntity entity = ProjectMapper.toEntity(project);
        ProjectEntity savedEntity = projectJpaRepository.save(entity);
        log.debug("Project saved successfully: id={}, name={}", savedEntity.getId(), savedEntity.getName());
        return ProjectMapper.toOutput(savedEntity);
    }

    @Override
    public List<ProjectOutput> findAll() {
        log.debug("Fetching all projects");
        List<ProjectOutput> projects = projectJpaRepository.findAll().stream()
                .map(ProjectMapper::toOutput)
                .collect(Collectors.toList());
        log.debug("Found {} projects", projects.size());
        return projects;
    }

    @Override
    public ProjectOutput findByName(String projectName) {
        log.debug("Searching for project by name: {}", projectName);
        return projectJpaRepository.findByName(projectName)
                .map(project -> {
                    log.debug("Found project: id={}, name={}", project.getId(), project.getName());
                    return ProjectMapper.toOutput(project);
                })
                .orElseGet(() -> {
                    log.debug("Project not found with name: {}", projectName);
                    return null;
                });
    }

    @Override
    public Optional<Project> findById(Long id) {
        log.debug("Searching for project by id: {}", id);
        ProjectEntity entity = projectJpaRepository.findById(id).orElse(null);
        if (entity == null) {
            log.debug("Project not found with id: {}", id);
            return Optional.empty();
        }
        log.debug("Found project: id={}, name={}", entity.getId(), entity.getName());
        return Optional.of(ProjectMapper.toDomain(entity));
    }

}
