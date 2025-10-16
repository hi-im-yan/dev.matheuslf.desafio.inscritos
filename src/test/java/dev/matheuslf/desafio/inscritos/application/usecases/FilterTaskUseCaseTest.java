package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class FilterTaskUseCaseTest {

    @Mock
    private ITasksRepository tasksRepository;

    @InjectMocks
    private FilterTaskUseCase filterTaskUseCase;

    private static final Long PROJECT_ID = 1L;
    private static final TaskStatusEnum STATUS = TaskStatusEnum.TODO;
    private static final TaskPriorityEnum PRIORITY = TaskPriorityEnum.HIGH;

    @Test
    void execute_WithAllFilters_ShouldReturnFilteredTasks() {
        // Arrange
        List<TaskOutput> expectedTasks = List.of(
            new TaskOutput(1L, "Task 1", "Description 1", STATUS, PRIORITY, LocalDate.now(), PROJECT_ID),
            new TaskOutput(2L, "Task 2", "Description 2", STATUS, PRIORITY, LocalDate.now(), PROJECT_ID)
        );
        when(tasksRepository.findWithFilter(STATUS, PRIORITY, PROJECT_ID)).thenReturn(expectedTasks);

        // Act
        List<TaskOutput> result = filterTaskUseCase.execute(STATUS, PRIORITY, PROJECT_ID);

        // Assert
        assertEquals(expectedTasks.size(), result.size());
        verify(tasksRepository).findWithFilter(STATUS, PRIORITY, PROJECT_ID);
    }

    @Test
    void execute_WithNullFilters_ShouldReturnAllTasks() {
        // Arrange
        List<TaskOutput> expectedTasks = List.of(
            new TaskOutput(1L, "Task 1", "Description 1", STATUS, PRIORITY, LocalDate.now(), PROJECT_ID),
            new TaskOutput(2L, "Task 2", "Description 2", TaskStatusEnum.DOING, TaskPriorityEnum.MEDIUM, LocalDate.now(), 2L)
        );
        when(tasksRepository.findWithFilter(null, null, null)).thenReturn(expectedTasks);

        // Act
        List<TaskOutput> result = filterTaskUseCase.execute(null, null, null);

        // Assert
        assertEquals(expectedTasks.size(), result.size());
        verify(tasksRepository).findWithFilter(null, null, null);
    }

    @Test
    void execute_WithStatusFilterOnly_ShouldReturnTasksWithMatchingStatus() {
        // Arrange
        List<TaskOutput> expectedTasks = List.of(
            new TaskOutput(1L, "Task 1", "Description 1", STATUS, PRIORITY, LocalDate.now(), PROJECT_ID)
        );
        when(tasksRepository.findWithFilter(STATUS, null, null)).thenReturn(expectedTasks);

        // Act
        List<TaskOutput> result = filterTaskUseCase.execute(STATUS, null, null);

        // Assert
        assertEquals(1, result.size());
        assertEquals(STATUS, result.get(0).status());
        verify(tasksRepository).findWithFilter(STATUS, null, null);
    }
}
