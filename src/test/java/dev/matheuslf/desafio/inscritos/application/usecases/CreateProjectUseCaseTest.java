package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.AlreadyExistsException;
import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CreateProjectUseCaseTest {

    @Mock
    private IProjectRepository projectRepository;

    @InjectMocks
    private CreateProjectUseCase createProjectUseCase;

    private Project project;
    private ProjectOutput projectOutput;

    @BeforeEach
    void setUp() {
        project = new Project(
                "Test Project",
                "Test Description",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10)
        );

        projectOutput = new ProjectOutput(
                project.name(),
                project.description(),
                project.startDate(),
                project.endDate()
        );
    }

    @Test
    void execute_WithNewProject_ReturnsCreatedProject() {
        // Arrange
        when(projectRepository.findByName(project.name())).thenReturn(null);
        when(projectRepository.save(project)).thenReturn(projectOutput);

        // Act
        ProjectOutput result = createProjectUseCase.execute(project);

        // Assert
        assertNotNull(result);
        assertEquals(project.name(), result.name());
        assertEquals(project.description(), result.description());
        verify(projectRepository, times(1)).findByName(project.name());
        verify(projectRepository, times(1)).save(project);
    }

    @Test
    void execute_WithExistingProjectName_ThrowsAlreadyExistsException() {
        // Arrange
        when(projectRepository.findByName(project.name())).thenReturn(projectOutput);

        // Act & Assert
        AlreadyExistsException exception = assertThrows(
                AlreadyExistsException.class,
                () -> createProjectUseCase.execute(project)
        );

        assertEquals("Project already exists", exception.getMessage());
        verify(projectRepository, times(1)).findByName(project.name());
        verify(projectRepository, never()).save(any(Project.class));
    }
}
