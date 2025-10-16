package dev.matheuslf.desafio.inscritos.infra.mappers;

import dev.matheuslf.desafio.inscritos.application.usecases.dto.CreateTaskInput;
import dev.matheuslf.desafio.inscritos.application.usecases.dto.TaskOutput;
import dev.matheuslf.desafio.inscritos.domain.models.Task;
import dev.matheuslf.desafio.inscritos.domain.models.TaskPriorityEnum;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.ProjectEntity;
import dev.matheuslf.desafio.inscritos.infra.repositories.entities.TaskEntity;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

class TaskMapperTest {

    private static final Long TASK_ID = 1L;
    private static final String TASK_TITLE = "Test Task";
    private static final String TASK_DESCRIPTION = "Test Description";
    private static final TaskStatusEnum TASK_STATUS = TaskStatusEnum.TODO;
    private static final TaskPriorityEnum TASK_PRIORITY = TaskPriorityEnum.MEDIUM;
    private static final LocalDate DUE_DATE = LocalDate.now().plusDays(7);
    private static final Long PROJECT_ID = 1L;
    private static final String PROJECT_NAME = "Test Project";
    private static final String PROJECT_DESCRIPTION = "Project Description";
    private static final LocalDate PROJECT_START_DATE = LocalDate.now();
    private static final LocalDate PROJECT_END_DATE = LocalDate.now().plusMonths(1);

    private ProjectEntity projectEntity;

    @BeforeEach
    void setUp() {
        projectEntity = new ProjectEntity();
        projectEntity.setId(PROJECT_ID);
        projectEntity.setName(PROJECT_NAME);
        projectEntity.setDescription(PROJECT_DESCRIPTION);
        projectEntity.setStartDate(PROJECT_START_DATE);
        projectEntity.setEndDate(PROJECT_END_DATE);
    }

    @Test
    void toDomain_ShouldMapEntityToDomain() {
        // Arrange
        TaskEntity entity = createTaskEntity();

        // Act
        Task domain = TaskMapper.toDomain(entity);

        // Assert
        assertNotNull(domain);
        assertEquals(TASK_TITLE, domain.title());
        assertEquals(TASK_DESCRIPTION, domain.description());
        assertEquals(TASK_STATUS, domain.status());
        assertEquals(TASK_PRIORITY, domain.priority());
        assertEquals(DUE_DATE, domain.dueDate());
        assertEquals(PROJECT_ID, domain.projectId());
    }

    @Test
    void toEntity_ShouldMapDomainToEntity() {
        // Arrange
        Task domain = new Task(
            TASK_TITLE,
            TASK_DESCRIPTION,
            TASK_STATUS,
            TASK_PRIORITY,
            DUE_DATE,
            PROJECT_ID
        );

        // Act
        TaskEntity entity = TaskMapper.toEntity(domain, projectEntity);

        // Assert
        assertNotNull(entity);
        assertEquals(TASK_TITLE, entity.getTitle());
        assertEquals(TASK_DESCRIPTION, entity.getDescription());
        assertEquals(TASK_STATUS, entity.getStatus());
        assertEquals(TASK_PRIORITY, entity.getPriority());
        assertEquals(DUE_DATE, entity.getDueDate());
        assertEquals(projectEntity, entity.getProject());
    }

    @Test
    void toEntity_WithCreateTaskInput_ShouldMapToEntity() {
        // Arrange
        CreateTaskInput input = new CreateTaskInput(
            TASK_TITLE,
            TASK_DESCRIPTION,
            TASK_STATUS,
            TASK_PRIORITY,
            DUE_DATE,
            PROJECT_ID
        );

        // Act
        TaskEntity entity = TaskMapper.toEntity(input);

        // Assert
        assertNotNull(entity);
        assertEquals(TASK_TITLE, entity.getTitle());
        assertEquals(TASK_DESCRIPTION, entity.getDescription());
        assertEquals(TASK_STATUS, entity.getStatus());
        assertEquals(TASK_PRIORITY, entity.getPriority());
        assertEquals(DUE_DATE, entity.getDueDate());
        assertNull(entity.getProject()); // Project should be set by service
    }

    @Test
    void toEntity_WithCreateTaskInputAndNullStatusAndPriority_ShouldUseDefaults() {
        // Arrange
        CreateTaskInput input = new CreateTaskInput(
            TASK_TITLE,
            TASK_DESCRIPTION,
            null, // status
            null, // priority
            DUE_DATE,
            PROJECT_ID
        );

        // Act
        TaskEntity entity = TaskMapper.toEntity(input);

        // Assert
        assertNotNull(entity);
        assertEquals(TaskStatusEnum.TODO, entity.getStatus()); // Default status
        assertEquals(TaskPriorityEnum.MEDIUM, entity.getPriority()); // Default priority
    }

    @Test
    void toOutput_ShouldMapEntityToOutput() {
        // Arrange
        TaskEntity entity = createTaskEntity();

        // Act
        TaskOutput output = TaskMapper.toOutput(entity);

        // Assert
        assertNotNull(output);
        assertEquals(TASK_ID, output.id());
        assertEquals(TASK_TITLE, output.title());
        assertEquals(TASK_DESCRIPTION, output.description());
        assertEquals(TASK_STATUS, output.status());
        assertEquals(TASK_PRIORITY, output.priority());
        assertEquals(DUE_DATE, output.dueDate());
        assertEquals(PROJECT_ID, output.projectId());
    }

    private TaskEntity createTaskEntity() {
        TaskEntity entity = new TaskEntity();
        entity.setId(TASK_ID);
        entity.setTitle(TASK_TITLE);
        entity.setDescription(TASK_DESCRIPTION);
        entity.setStatus(TASK_STATUS);
        entity.setPriority(TASK_PRIORITY);
        entity.setDueDate(DUE_DATE);
        entity.setProject(projectEntity);
        return entity;
    }
}
