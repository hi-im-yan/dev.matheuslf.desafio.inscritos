package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DeleteTaskUseCaseTest {

    @Mock
    private ITasksRepository tasksRepository;

    @InjectMocks
    private DeleteTaskUseCase deleteTaskUseCase;

    private static final Long TASK_ID = 1L;
    private static final Long NON_EXISTENT_TASK_ID = 999L;

    @BeforeEach
    void setUp() {
    }

    @Test
    void execute_WithExistingTask_ShouldDeleteTask() {
        // Arrange
        when(tasksRepository.findById(TASK_ID)).thenReturn(new TaskOutput(
                TASK_ID, "Test Task", "Description", null, null, null, null));
        // Act & Assert
        deleteTaskUseCase.execute(TASK_ID);
        verify(tasksRepository).deleteById(TASK_ID);
    }

    @Test
    void execute_WithNonExistentTask_ShouldThrowNotFoundException() {
        // Arrange
        when(tasksRepository.findById(NON_EXISTENT_TASK_ID)).thenReturn(null);

        // Act & Assert
        assertThrows(NotFoundException.class,
                () -> deleteTaskUseCase.execute(NON_EXISTENT_TASK_ID));
    }

}
