package dev.matheuslf.desafio.inscritos.infra.mappers;

import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProjectMapperTest {

    private static final Long PROJECT_ID = 1L;
    private static final String PROJECT_NAME = "Test Project";
    private static final String PROJECT_DESCRIPTION = "Test Description";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusMonths(1);

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        // Arrange
        ProjectEntity entity = new ProjectEntity();
        entity.setId(PROJECT_ID);
        entity.setName(PROJECT_NAME);
        entity.setDescription(PROJECT_DESCRIPTION);
        entity.setStartDate(START_DATE);
        entity.setEndDate(END_DATE);

        // Act
        Project domain = ProjectMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(PROJECT_NAME, domain.name());
        assertEquals(PROJECT_DESCRIPTION, domain.description());
        assertEquals(START_DATE, domain.startDate());
        assertEquals(END_DATE, domain.endDate());
    }

    @Test
    void toOutput_ShouldMapEntityToOutput() {
        // Arrange
        ProjectEntity entity = new ProjectEntity();
        entity.setId(PROJECT_ID);
        entity.setName(PROJECT_NAME);
        entity.setDescription(PROJECT_DESCRIPTION);
        entity.setStartDate(START_DATE);
        entity.setEndDate(END_DATE);

        // Act
        ProjectOutput output = ProjectMapper.toOutput(entity);

        // Assert
        assertNotNull(output);
        assertEquals(PROJECT_ID, output.id());
        assertEquals(PROJECT_NAME, output.name());
        assertEquals(PROJECT_DESCRIPTION, output.description());
        assertEquals(START_DATE, output.startDate());
        assertEquals(END_DATE, output.endDate());
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        // Arrange
        Project domain = new Project(PROJECT_NAME, PROJECT_DESCRIPTION, START_DATE, END_DATE);

        // Act
        ProjectEntity entity = ProjectMapper.toEntity(domain);

        // Assert
        assertNotNull(entity);
        assertEquals(PROJECT_NAME, entity.getName());
        assertEquals(PROJECT_DESCRIPTION, entity.getDescription());
        assertEquals(START_DATE, entity.getStartDate());
        assertEquals(END_DATE, entity.getEndDate());
    }

}
