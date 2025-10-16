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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProjectRepositoryTest {

    @Mock
    private ProjectJpaRepository projectJpaRepository;

    @InjectMocks
    private ProjectRepository projectRepository;

    private static final Long PROJECT_ID = 1L;
    private static final String PROJECT_NAME = "Test Project";
    private static final String PROJECT_DESCRIPTION = "Test Description";
    private static final LocalDate START_DATE = LocalDate.now();
    private static final LocalDate END_DATE = LocalDate.now().plusMonths(1);

    private Project project;
    private ProjectEntity projectEntity;
    private ProjectEntity savedProjectEntity;

    @BeforeEach
    void setUp() {
        project = new Project(
            PROJECT_NAME,
            PROJECT_DESCRIPTION,
            START_DATE,
            END_DATE
        );

        projectEntity = ProjectEntity.builder()
            .name(PROJECT_NAME)
            .description(PROJECT_DESCRIPTION)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();

        savedProjectEntity = ProjectEntity.builder()
            .id(PROJECT_ID)
            .name(PROJECT_NAME)
            .description(PROJECT_DESCRIPTION)
            .startDate(START_DATE)
            .endDate(END_DATE)
            .build();
    }

    @Test
    void save_WithNewProject_ShouldReturnSavedProject() {
        // Arrange
        when(projectJpaRepository.save(any(ProjectEntity.class))).thenReturn(savedProjectEntity);

        // Act
        ProjectOutput result = projectRepository.save(project);

        // Assert
        assertNotNull(result);
        assertEquals(PROJECT_ID, result.id());
        assertEquals(PROJECT_NAME, result.name());
        assertEquals(PROJECT_DESCRIPTION, result.description());
        assertEquals(START_DATE, result.startDate());
        assertEquals(END_DATE, result.endDate());

        verify(projectJpaRepository, times(1)).save(any(ProjectEntity.class));
    }

    @Test
    void findAll_ShouldReturnListOfProjects() {
        // Arrange
        List<ProjectEntity> projectEntities = Arrays.asList(
            savedProjectEntity,
            ProjectEntity.builder()
                .id(2L)
                .name("Another Project")
                .description("Another Description")
                .startDate(START_DATE.plusDays(1))
                .endDate(END_DATE.plusDays(1))
                .build()
        );
        when(projectJpaRepository.findAll()).thenReturn(projectEntities);

        // Act
        List<ProjectOutput> result = projectRepository.findAll();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(PROJECT_ID, result.get(0).id());
        assertEquals("Another Project", result.get(1).name());

        verify(projectJpaRepository, times(1)).findAll();
    }

    @Test
    void findByName_WithExistingName_ShouldReturnProject() {
        // Arrange
        when(projectJpaRepository.findByName(PROJECT_NAME))
            .thenReturn(Optional.of(savedProjectEntity));

        // Act
        ProjectOutput result = projectRepository.findByName(PROJECT_NAME);

        // Assert
        assertNotNull(result);
        assertEquals(PROJECT_ID, result.id());
        assertEquals(PROJECT_NAME, result.name());

        verify(projectJpaRepository, times(1)).findByName(PROJECT_NAME);
    }

    @Test
    void findByName_WithNonExistingName_ShouldReturnNull() {
        // Arrange
        when(projectJpaRepository.findByName("NonExisting"))
            .thenReturn(Optional.empty());

        // Act
        ProjectOutput result = projectRepository.findByName("NonExisting");

        // Assert
        assertNull(result);
        verify(projectJpaRepository, times(1)).findByName("NonExisting");
    }

    @Test
    void findById_WithExistingId_ShouldReturnProject() {
        // Arrange
        when(projectJpaRepository.findById(PROJECT_ID))
            .thenReturn(Optional.of(savedProjectEntity));

        // Act
        Optional<Project> result = projectRepository.findById(PROJECT_ID);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(PROJECT_NAME, result.get().name());
        assertEquals(PROJECT_DESCRIPTION, result.get().description());

        verify(projectJpaRepository, times(1)).findById(PROJECT_ID);
    }

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Arrange
        when(projectJpaRepository.findById(999L))
            .thenReturn(Optional.empty());

        // Act
        Optional<Project> result = projectRepository.findById(999L);

        // Assert
        assertTrue(result.isEmpty());
        verify(projectJpaRepository, times(1)).findById(999L);
    }
}
