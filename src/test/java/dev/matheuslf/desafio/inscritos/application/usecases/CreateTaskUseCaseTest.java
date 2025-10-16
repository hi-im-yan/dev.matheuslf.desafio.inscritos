package dev.matheuslf.desafio.inscritos.application.usecases;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.interfaces.IProjectRepository;
import dev.matheuslf.desafio.inscritos.application.interfaces.ITasksRepository;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateTaskInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.exceptions.NotValidException;
import dev.matheuslf.desafio.inscritos.domain.models.Project;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CreateTaskUseCaseTest {

    @Mock
    private ITasksRepository taskRepository;

    @Mock
    private IProjectRepository projectRepository;

    @InjectMocks
    private CreateTaskUseCase createTaskUseCase;

    private Project project;
    private CreateTaskInput validInput;
    private TaskOutput expectedOutput;
    private final LocalDate projectStartDate = LocalDate.of(2023, 1, 1);
    private final LocalDate validDueDate = LocalDate.of(2023, 12, 31);
    private final Long projectId = 1L;

    @BeforeEach
    void setUp() {
        project = new Project("Test Project", "Test Description", projectStartDate, null);
        
        validInput = new CreateTaskInput(
            "Test Task",
            "Test Description",
            TaskStatusEnum.TODO,
            TaskPriorityEnum.MEDIUM,
            validDueDate,
            projectId
        );

        expectedOutput = new TaskOutput(
            1L,
            "Test Task",
            "Test Description",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.MEDIUM,
            validDueDate,
            projectId
        );
    }

    @Test
    void execute_WithValidInput_ShouldCreateTask() {
        // Arrange
        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.of(project));
        when(taskRepository.save(any())).thenReturn(expectedOutput);

        // Act
        TaskOutput result = createTaskUseCase.execute(validInput);

        // Assert
        assertNotNull(result);
        assertEquals(expectedOutput.id(), result.id());
        assertEquals(expectedOutput.title(), result.title());
        assertEquals(expectedOutput.status(), result.status());
        verify(projectRepository, times(1)).findById(projectId);
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    void execute_WithNonExistentProject_ShouldThrowNotFoundException() {
        // Arrange
        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> createTaskUseCase.execute(validInput));
        verify(projectRepository, times(1)).findById(projectId);
        verifyNoInteractions(taskRepository);
    }

    @Test
    void execute_WithInvalidDueDate_ShouldThrowNotValidException() {
        // Arrange
        LocalDate invalidDueDate = projectStartDate.minusDays(1);
        CreateTaskInput invalidInput = new CreateTaskInput(
            "Test Task",
            "Test Description",
                TaskStatusEnum.TODO,
                TaskPriorityEnum.MEDIUM,
            invalidDueDate,
            projectId
        );
        
        when(projectRepository.findById(projectId)).thenReturn(java.util.Optional.of(project));

        // Act & Assert
        assertThrows(NotValidException.class, () -> createTaskUseCase.execute(invalidInput));
        verify(projectRepository, times(1)).findById(projectId);
        verifyNoInteractions(taskRepository);
    }
}
