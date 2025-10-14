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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ProjectTest {
    
    private static final String VALID_NAME = "Test Project";
    private static final String VALID_DESCRIPTION = "This is a test project";
    private static final LocalDate VALID_START_DATE = LocalDate.now().plusDays(1);
    
    @Test
    void shouldCreateProjectWithValidParameters() {
        // when
        Project project = new Project(VALID_NAME, VALID_DESCRIPTION, VALID_START_DATE);
        
        // then
        assertNotNull(project);
        assertEquals(VALID_NAME, project.getName());
        assertEquals(VALID_DESCRIPTION, project.getDescription());
        assertEquals(VALID_START_DATE, project.getStartDate());
        assertNull(project.getEndDate());
    }
    

    @ParameterizedTest
    @MethodSource("invalidNames")
    void shouldThrowExceptionWhenNameIsInvalid(String name, String expectedMessage) {
        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Project(name, VALID_DESCRIPTION, VALID_START_DATE)
        );
        assertEquals(expectedMessage, exception.getMessage());
    }
    
    @ParameterizedTest
    @NullSource
    void shouldThrowExceptionWhenStartDateIsNull(LocalDate startDate) {
        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Project(VALID_NAME, VALID_DESCRIPTION, startDate)
        );
        assertEquals("Start date must be a valid date.", exception.getMessage());
    }
    
    @Test
    void shouldThrowExceptionWhenCreatingProjectWithInvalidEndDate() {
        // given
        LocalDate startDate = VALID_START_DATE;
        LocalDate endDate = startDate.minusDays(1);

        // when & then
        NotValidException exception = assertThrows(
            NotValidException.class,
            () -> new Project(VALID_NAME, VALID_DESCRIPTION, startDate, endDate)
        );

        assertEquals("End date must be after start date.", exception.getMessage());
    }
    
    private static Stream<Arguments> invalidNames() {
        return Stream.of(
            Arguments.of(null, "Name must be between 3 and 100 characters."),
            Arguments.of("", "Name must be between 3 and 100 characters."),
            Arguments.of("  ", "Name must be between 3 and 100 characters."),
            Arguments.of("ab", "Name must be between 3 and 100 characters."),
            Arguments.of("a".repeat(101), "Name must be between 3 and 100 characters.")
        );
    }
}