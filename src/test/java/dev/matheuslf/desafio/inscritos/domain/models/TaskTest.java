package dev.matheuslf.desafio.inscritos.domain.models;

import dev.matheuslf.desafio.inscritos.domain.exceptions.NotValidException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    
    private static final String VALID_TITLE = "Implement user authentication";
    private static final String VALID_DESCRIPTION = "Implement JWT based authentication";
    private static final TaskStatusEnum VALID_STATUS = TaskStatusEnum.TODO;
    private static final TaskPriorityEnum VALID_PRIORITY = TaskPriorityEnum.HIGH;
    private static final LocalDate PROJECT_START_DATE = LocalDate.now().plusDays(1);
    private static final LocalDate VALID_DUE_DATE = PROJECT_START_DATE.plusDays(5);
    private Project validProject;
    
    @BeforeEach
    void setUp() {
        validProject = new Project("Test Project", "Test Description", PROJECT_START_DATE);
    }
    
    @Test
    void shouldCreateTaskWithValidParameters() {
        // when
        Task task = new Task(VALID_TITLE, VALID_DESCRIPTION, VALID_STATUS, VALID_PRIORITY, VALID_DUE_DATE, validProject);
        
        // then
        assertNotNull(task);
        assertEquals(VALID_TITLE, task.getTitle());
        assertEquals(VALID_DESCRIPTION, task.getDescription());
        assertEquals(VALID_STATUS, task.getStatus());
        assertEquals(VALID_PRIORITY, task.getPriority());
        assertEquals(VALID_DUE_DATE, task.getDueDate());
        assertEquals(validProject, task.getProject());
    }
    
    @Test
    void shouldCreateTaskWithNullDescription() {
        // when
        Task task = new Task(VALID_TITLE, null, VALID_STATUS, VALID_PRIORITY, null, validProject);
        
        // then
        assertNull(task.getDescription());
        assertNull(task.getDueDate());
    }
    
    @ParameterizedTest
    @MethodSource("invalidTitles")
    void shouldThrowExceptionWhenTitleIsInvalid(String title, String expectedMessage) {
        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Task(title, VALID_DESCRIPTION, VALID_STATUS, VALID_PRIORITY, VALID_DUE_DATE, validProject)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenStatusIsNull(TaskStatusEnum status) {
        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Task(VALID_TITLE, VALID_DESCRIPTION, status, VALID_PRIORITY, VALID_DUE_DATE, validProject)
        );
        assertEquals("Status is required", exception.getMessage());
    }
    
    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenPriorityIsNull(TaskPriorityEnum priority) {
        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Task(VALID_TITLE, VALID_DESCRIPTION, VALID_STATUS, priority, VALID_DUE_DATE, validProject)
        );
        assertEquals("Priority is required", exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWhenProjectIsNull() {
        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Task(VALID_TITLE, VALID_DESCRIPTION, VALID_STATUS, VALID_PRIORITY, VALID_DUE_DATE, null)
        );
        assertEquals("Project is required", exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWhenDueDateIsBeforeProjectStartDate() {
        // given
        LocalDate invalidDueDate = PROJECT_START_DATE.minusDays(1);
        
        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Task(VALID_TITLE, VALID_DESCRIPTION, VALID_STATUS, VALID_PRIORITY, invalidDueDate, validProject)
        );
        assertEquals("Due date cannot be before the project start date", exception.getMessage());
    }
    
    private static Stream<Arguments> invalidTitles() {
        return Stream.of(
            Arguments.of(null, "Title must be between 5 and 150 characters"),
            Arguments.of("", "Title must be between 5 and 150 characters"),
            Arguments.of("    ", "Title must be between 5 and 150 characters"),
            Arguments.of("test", "Title must be between 5 and 150 characters"),
            Arguments.of("a".repeat(151), "Title must be between 5 and 150 characters")
        );
    }
}
