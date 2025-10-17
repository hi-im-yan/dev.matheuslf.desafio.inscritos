package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.UpdateTaskStatusInput;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UpdateTaskStatusUseCaseTest {

    @Mock
    private ITasksRepository tasksRepository;

    @InjectMocks
    private UpdateTaskStatusUseCase updateTaskStatusUseCase;

    private final Long TASK_ID = 1L;
    private final String TASK_TITLE = "Test Task";
    private final String TASK_DESCRIPTION = "Test Description";
    private TaskOutput existingTask;

    @BeforeEach
    void setUp() {
        existingTask = new TaskOutput(
            TASK_ID,
            TASK_TITLE,
            TASK_DESCRIPTION,
            TaskStatusEnum.TODO,
            null,
            null,
            null
        );
    }

    @Test
    void shouldUpdateTaskStatusSuccessfully() {
        // Arrange
        TaskStatusEnum newStatus = TaskStatusEnum.DOING;
        UpdateTaskStatusInput input = new UpdateTaskStatusInput(TASK_ID, newStatus);
        
        TaskOutput updatedTask = new TaskOutput(
            TASK_ID,
            TASK_TITLE,
            TASK_DESCRIPTION,
            newStatus,
            null,
            null,
            null
        );

        when(tasksRepository.findById(TASK_ID)).thenReturn(existingTask);
        when(tasksRepository.updateStatus(TASK_ID, newStatus)).thenReturn(updatedTask);

        // Act
        TaskOutput result = updateTaskStatusUseCase.execute(input);

        // Assert
        assertNotNull(result);
        assertEquals(TASK_ID, result.id());
        assertEquals(TASK_TITLE, result.title());
        assertEquals(TASK_DESCRIPTION, result.description());
        assertEquals(newStatus, result.status());
        
        verify(tasksRepository, times(1)).findById(TASK_ID);
        verify(tasksRepository, times(1)).updateStatus(TASK_ID, newStatus);
    }

    @Test
    void shouldThrowExceptionWhenTaskNotFound() {
        // Arrange
        UpdateTaskStatusInput input = new UpdateTaskStatusInput(999L, TaskStatusEnum.DONE);
        when(tasksRepository.findById(999L)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class, () -> updateTaskStatusUseCase.execute(input));
        verify(tasksRepository, times(1)).findById(999L);
        verify(tasksRepository, never()).updateStatus(anyLong(), any());
    }
}
