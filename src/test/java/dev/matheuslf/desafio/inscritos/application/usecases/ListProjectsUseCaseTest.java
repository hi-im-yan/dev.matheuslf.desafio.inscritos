package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ListProjectsUseCaseTest {

    @Mock
    private IProjectRepository projectRepository;

    @InjectMocks
    private ListProjectsUseCase listProjectsUseCase;

    private List<ProjectOutput> testProjects;

    @BeforeEach
    void setUp() {
        testProjects = Arrays.asList(
            new ProjectOutput(
                "Project 1",
                "First test project",
                LocalDate.now().plusDays(1),
                LocalDate.now().plusDays(10)
            ),
            new ProjectOutput(
                "Project 2",
                "Second test project",
                LocalDate.now().plusDays(2),
                null
            )
        );
    }

    @Test
    void execute_WhenProjectsExist_ReturnsListOfProjects() {
        // Arrange
        when(projectRepository.findAll()).thenReturn(testProjects);

        // Act
        List<ProjectOutput> result = listProjectsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Project 1", result.get(0).name());
        assertEquals("Project 2", result.get(1).name());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void execute_WhenNoProjectsExist_ReturnsEmptyList() {
        // Arrange
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ProjectOutput> result = listProjectsUseCase.execute();

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(projectRepository, times(1)).findAll();
    }

    @Test
    void execute_WhenRepositoryThrowsException_PropagatesException() {
        // Arrange
        when(projectRepository.findAll()).thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> listProjectsUseCase.execute());
        verify(projectRepository, times(1)).findAll();
    }
}
