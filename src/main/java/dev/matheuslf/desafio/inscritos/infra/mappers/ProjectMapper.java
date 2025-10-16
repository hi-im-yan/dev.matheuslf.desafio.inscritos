package dev.matheuslf.desafio.inscritos.infra.mappers;

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
