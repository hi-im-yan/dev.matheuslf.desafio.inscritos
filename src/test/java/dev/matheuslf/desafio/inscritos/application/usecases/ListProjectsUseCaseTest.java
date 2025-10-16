package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.ProjectOutput;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ListProjectsUseCaseTest {

    @Mock
    private IProjectRepository projectRepository;

    @InjectMocks
    private ListProjectsUseCase listProjectsUseCase;

    @Test
    void execute_ShouldReturnListOfProjects() {
        // Arrange
        List<ProjectOutput> expectedProjects = List.of(
            new ProjectOutput(1L, "Project 1", "Description 1",LocalDate.now(), null),
            new ProjectOutput(2L, "Project 2", "Description 2", LocalDate.now(), null)
        );
        when(projectRepository.findAll()).thenReturn(expectedProjects);

        // Act
        List<ProjectOutput> result = listProjectsUseCase.execute();

        // Assert
        assertEquals(expectedProjects.size(), result.size());
        assertEquals(expectedProjects.get(0).name(), result.get(0).name());
        verify(projectRepository).findAll();
    }

    @Test
    void execute_WhenNoProjects_ShouldReturnEmptyList() {
        // Arrange
        when(projectRepository.findAll()).thenReturn(Collections.emptyList());

        // Act
        List<ProjectOutput> result = listProjectsUseCase.execute();

        // Assert
        assertTrue(result.isEmpty());
        verify(projectRepository).findAll();
    }
}
