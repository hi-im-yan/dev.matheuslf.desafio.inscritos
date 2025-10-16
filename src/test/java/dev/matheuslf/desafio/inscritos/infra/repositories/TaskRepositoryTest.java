package dev.matheuslf.desafio.inscritos.infra.repositories;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Task;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.TaskEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.ProjectJpaRepository;
import dev.matheuslf.desafio.inscritos.infra.repositories.jparepository.TaskJpaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class TaskRepositoryTest {

    @Mock
    private TaskJpaRepository taskJpaRepository;

    @Mock
    private ProjectJpaRepository projectJpaRepository;

    @InjectMocks
    private TaskRepository taskRepository;

    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "Test Task";
    private static final String TASK_DESCRIPTION = "Test Description";
    private static final TaskStatusEnum TASK_STATUS = TaskStatusEnum.TODO;
    private static final TaskStatusEnum UPDATED_STATUS = TaskStatusEnum.DOING;
    private static final TaskPriorityEnum TASK_PRIORITY = TaskPriorityEnum.MEDIUM;
    private static final LocalDate DUE_DATE = LocalDate.now().plusDays(7);
    private static final Long PROJECT_ID = 1L;
    private static final String PROJECT_NAME = "Test Project";

    private Task task;
    private ProjectEntity projectEntity;
    private TaskEntity taskEntity;
    private TaskEntity savedTaskEntity;

    @BeforeEach
    void setUp() {
        task = new Task(
            TASK_TITLE,
            TASK_DESCRIPTION,
            TASK_STATUS,
            TASK_PRIORITY,
            DUE_DATE,
            PROJECT_ID
        );

        projectEntity = ProjectEntity.builder()
            .id(PROJECT_ID)
            .name(PROJECT_NAME)
            .startDate(LocalDate.now())
            .build();

        taskEntity = TaskEntity.builder()
            .title(TASK_TITLE)
            .description(TASK_DESCRIPTION)
            .status(TASK_STATUS)
            .priority(TASK_PRIORITY)
            .dueDate(DUE_DATE)
            .project(projectEntity)
            .build();

        savedTaskEntity = TaskEntity.builder()
            .id(TASK_ID)
            .title(TASK_TITLE)
            .description(TASK_DESCRIPTION)
            .status(TASK_STATUS)
            .priority(TASK_PRIORITY)
            .dueDate(DUE_DATE)
            .project(projectEntity)
            .build();
    }

    @Test
    void save_WithValidTask_ShouldReturnSavedTask() {
        // Arrange
        when(projectJpaRepository.findById(PROJECT_ID)).thenReturn(Optional.of(projectEntity));
        when(taskJpaRepository.save(any(TaskEntity.class))).thenReturn(savedTaskEntity);

        // Act
        TaskOutput result = taskRepository.save(task);

        // Assert
        assertNotNull(result);
        assertEquals(TASK_ID, result.id());
        assertEquals(TASK_TITLE, result.title());
        assertEquals(TASK_DESCRIPTION, result.description());
        assertEquals(TASK_STATUS, result.status());
        assertEquals(TASK_PRIORITY, result.priority());
        assertEquals(DUE_DATE, result.dueDate());
        assertEquals(PROJECT_ID, result.projectId());

        verify(projectJpaRepository, times(1)).findById(PROJECT_ID);
        verify(taskJpaRepository, times(1)).save(any(TaskEntity.class));
    }

    @Test
    void save_WithNonExistingProject_ShouldThrowNotFoundException() {
        // Arrange
        when(projectJpaRepository.findById(PROJECT_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, () -> taskRepository.save(task));
        verify(projectJpaRepository, times(1)).findById(PROJECT_ID);
        verify(taskJpaRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void findWithFilter_ShouldReturnFilteredTasks() {
        // Arrange
        TaskEntity filteredTask = TaskEntity.builder()
            .id(2L)
            .title("Filtered Task")
            .status(TASK_STATUS)
            .priority(TASK_PRIORITY)
            .project(projectEntity)
            .build();

        when(taskJpaRepository.findByProjectId(PROJECT_ID)).thenReturn(List.of(taskEntity, filteredTask));

        // Act
        List<TaskOutput> result = taskRepository.findWithFilter(TASK_STATUS, TASK_PRIORITY, PROJECT_ID);

        // Assert
        assertEquals(2, result.size());
        assertEquals(TASK_TITLE, result.get(0).title());
        assertEquals("Filtered Task", result.get(1).title());
        
        verify(taskJpaRepository, times(1)).findByProjectId(PROJECT_ID);
    }

    @Test
    void findWithFilter_WithNoMatchingTasks_ShouldReturnEmptyList() {
        // Arrange
        when(taskJpaRepository.findByProjectId(PROJECT_ID)).thenReturn(Collections.emptyList());

        // Act
        List<TaskOutput> result = taskRepository.findWithFilter(TASK_STATUS, TASK_PRIORITY, PROJECT_ID);

        // Assert
        assertTrue(result.isEmpty());
        verify(taskJpaRepository, times(1)).findByProjectId(PROJECT_ID);
    }

    @Test
    void updateStatus_WithExistingTask_ShouldUpdateAndReturnTask() {
        // Arrange
        TaskEntity updatedTask = TaskEntity.builder()
            .id(TASK_ID)
            .title(TASK_TITLE)
            .status(UPDATED_STATUS)
            .priority(TASK_PRIORITY)
            .project(projectEntity)
            .build();

        when(taskJpaRepository.findById(TASK_ID)).thenReturn(Optional.of(taskEntity));
        when(taskJpaRepository.save(any(TaskEntity.class))).thenReturn(updatedTask);

        // Act
        TaskOutput result = taskRepository.updateStatus(TASK_ID, UPDATED_STATUS);

        // Assert
        assertNotNull(result);
        assertEquals(TASK_ID, result.id());
        assertEquals(UPDATED_STATUS, result.status());
        
        verify(taskJpaRepository, times(1)).findById(TASK_ID);
        verify(taskJpaRepository, times(1)).save(taskEntity);
        assertEquals(UPDATED_STATUS, taskEntity.getStatus());
    }

    @Test
    void updateStatus_WithNonExistingTask_ShouldThrowNotFoundException() {
        // Arrange
        when(taskJpaRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(NotFoundException.class, 
            () -> taskRepository.updateStatus(TASK_ID, UPDATED_STATUS));
        
        verify(taskJpaRepository, times(1)).findById(TASK_ID);
        verify(taskJpaRepository, never()).save(any(TaskEntity.class));
    }

    @Test
    void deleteById_ShouldCallRepositoryDelete() {
        // Act
        taskRepository.deleteById(TASK_ID);

        // Assert
        verify(taskJpaRepository, times(1)).deleteById(TASK_ID);
    }

    @Test
    void findById_WithExistingTask_ShouldReturnTask() {
        // Arrange
        when(taskJpaRepository.findById(TASK_ID)).thenReturn(Optional.of(savedTaskEntity));

        // Act
        TaskOutput result = taskRepository.findById(TASK_ID);

        // Assert
        assertNotNull(result);
        assertEquals(TASK_ID, result.id());
        assertEquals(TASK_TITLE, result.title());
        
        verify(taskJpaRepository, times(1)).findById(TASK_ID);
    }

    @Test
    void findById_WithNonExistingTask_ShouldReturnNull() {
        // Arrange
        when(taskJpaRepository.findById(TASK_ID)).thenReturn(Optional.empty());

        // Act
        TaskOutput result = taskRepository.findById(TASK_ID);

        // Assert
        assertNull(result);
        verify(taskJpaRepository, times(1)).findById(TASK_ID);
    }
}
