package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.AlreadyExistsException;
import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateProjectInput;
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
                1L,
                project.name(),
                project.description(),
                project.startDate(),
                project.endDate()
        );
    }


    @Test
    void execute_WithNewProject_ReturnsCreatedProject() {
        // Arrange
        CreateProjectInput input = new CreateProjectInput(
                project.name(),
                project.description(),
                project.startDate(),
                project.endDate()
        );
        when(projectRepository.findByName(input.name())).thenReturn(null);
        when(projectRepository.save(any(Project.class))).thenReturn(projectOutput);

        // Act
        ProjectOutput result = createProjectUseCase.execute(input);

        // Assert
        assertNotNull(result);
        assertEquals(project.name(), result.name());
        assertEquals(project.description(), result.description());
        verify(projectRepository, times(1)).findByName(project.name());
        verify(projectRepository, times(1)).save(any(Project.class));
    }

    @Test
    void execute_WithExistingProjectName_ThrowsAlreadyExistsException() {
        // Arrange
        when(projectRepository.findByName(project.name())).thenReturn(projectOutput);

        // Act & Assert
        CreateProjectInput input = new CreateProjectInput(
                project.name(),
                project.description(),
                project.startDate(),
                project.endDate()
        );
        AlreadyExistsException exception = assertThrows(
                AlreadyExistsException.class,
                () -> createProjectUseCase.execute(input)
        );

        assertEquals("Project already exists", exception.getMessage());
        verify(projectRepository, times(1)).findByName(project.name());
        verify(projectRepository, never()).save(any(Project.class));
    }
}
