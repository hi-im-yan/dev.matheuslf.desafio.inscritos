package dev.matheuslf.desafio.inscritos.infra.repositories;

import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.ProjectJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProjectRepositoryTest {

    @Mock
    private ProjectJpaRepository projectJpaRepository;

    @InjectMocks
    private ProjectRepository projectRepository;

    private Project project;
    private ProjectEntity projectEntity;
    private ProjectOutput projectOutput;

    @BeforeEach
    void setUp() {
        project = new Project(
                "Test Project",
                "Test Description",
                LocalDate.now().plusDays(1)
        );

        projectEntity = ProjectEntity.builder()
                .id(1L)
                .name(project.name())
                .description(project.description())
                .startDate(project.startDate())
                .endDate(project.endDate())
                .build();

        projectOutput = new ProjectOutput(
                projectEntity.getName(),
                projectEntity.getDescription(),
                projectEntity.getStartDate(),
                projectEntity.getEndDate()
        );
    }

    @Test
    void save_WithValidProject_ReturnsProjectOutput() {
        // Arrange
        when(projectJpaRepository.save(any(ProjectEntity.class))).thenReturn(projectEntity);

        // Act
        ProjectOutput result = projectRepository.save(project);

        // Assert
        assertNotNull(result);
        assertEquals(project.name(), result.name());
        assertEquals(project.description(), result.description());
        verify(projectJpaRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void findAll_WhenProjectsExist_ReturnsListOfProjectOutputs() {
        // Arrange
        when(projectJpaRepository.findAll()).thenReturn(List.of(projectEntity));

        // Act
        List<ProjectOutput> result = projectRepository.findAll();

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals(project.name(), result.get(0).name());
        verify(projectJpaRepository, times(1)).findAll();
    }

    @Test
    void findByName_WhenProjectExists_ReturnsProjectOutput() {
        // Arrange
        when(projectJpaRepository.findByName(project.name())).thenReturn(Optional.of(projectEntity));

        // Act
        ProjectOutput result = projectRepository.findByName(project.name());

        // Assert
        assertNotNull(result);
        assertEquals(project.name(), result.name());
        verify(projectJpaRepository, times(1)).findByName(project.name());
    }

    @Test
    void findByName_WhenProjectDoesNotExist_ReturnsNull() {
        // Arrange
        when(projectJpaRepository.findByName(anyString())).thenReturn(Optional.empty());

        // Act
        ProjectOutput result = projectRepository.findByName("Non-existent Project");

        // Assert
        assertNull(result);
        verify(projectJpaRepository, times(1)).findByName(anyString());
    }
}
