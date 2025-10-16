package dev.matheuslf.desafio.inscritos.infra.mappers;

import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateProjectInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;

public class ProjectMapper {

    public static Project toDomain(ProjectEntity entity) {
        return new Project(
            entity.getName(),
            entity.getDescription(),
            entity.getStartDate(),
            entity.getEndDate()
        );
    }

    public static ProjectEntity toEntity(CreateProjectInput input) {
        var entity = new ProjectEntity();
        entity.setName(input.name());
        entity.setDescription(input.description());
        entity.setStartDate(input.startDate());
        entity.setEndDate(input.endDate());
        return entity;
    }

    public static ProjectEntity toEntity(ProjectOutput output) {
        var entity = new ProjectEntity();
        entity.setId(output.id());
        entity.setName(output.name());
        entity.setDescription(output.description());
        entity.setStartDate(output.startDate());
        entity.setEndDate(output.endDate());
        return entity;
    }

    public static ProjectOutput toOutput(ProjectEntity entity) {
        return new ProjectOutput(
            entity.getId(),
            entity.getName(),
            entity.getDescription(),
            entity.getStartDate(),
            entity.getEndDate()
        );
    }

    public static ProjectEntity toEntity(Project domain) {
        var entity = new ProjectEntity();
        entity.setName(domain.name());
        entity.setDescription(domain.description());
        entity.setStartDate(domain.startDate());
        entity.setEndDate(domain.endDate());
        return entity;
    }
}
