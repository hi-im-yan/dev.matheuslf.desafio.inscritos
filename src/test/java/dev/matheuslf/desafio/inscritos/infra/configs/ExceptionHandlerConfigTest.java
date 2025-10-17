package dev.matheuslf.desafio.inscritos.infra.configs;

import dev.matheuslf.desafio.inscritos.application.exceptions.NotFoundException;
import dev.matheuslf.desafio.inscritos.domain.models.TaskStatusEnum;
import dev.matheuslf.desafio.inscritos.infra.controllers.dto.ApiErrorResDTO;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ExceptionHandlerConfigTest {

    @InjectMocks
    private ExceptionHandlerConfig exceptionHandler;

    @Mock
    private HttpServletRequest request;

    @BeforeEach
    void setUp() {
        when(request.getRequestURI()).thenReturn("/api/tasks");
    }

    @Test
    void handleMethodArgumentTypeMismatch_NonEnumType_ShouldReturnBadRequest() {
        // Arrange
        MethodArgumentTypeMismatchException ex = mock(MethodArgumentTypeMismatchException.class);
        when(ex.getName()).thenReturn("id");
        when(ex.getValue()).thenReturn("not-a-number");
        when(ex.getRequiredType()).thenReturn((Class) Long.class);

        // Act
        ResponseEntity<ApiErrorResDTO> response = exceptionHandler.handleMethodArgumentTypeMismatch(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getMessage().contains("The parameter 'id' of value 'not-a-number' could not be converted to type 'Long'"));
    }

    @Test
    void handleNotFoundException_ShouldReturnNotFound() {
        // Arrange
        String errorMessage = "Task not found with id: 123";
        NotFoundException ex = new NotFoundException(errorMessage);

        // Act
        ResponseEntity<ApiErrorResDTO> response = exceptionHandler.handleNotFoundException(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(errorMessage, response.getBody().getMessage());
        assertEquals("/api/tasks", response.getBody().getPath());
        assertEquals(HttpStatus.NOT_FOUND.value(), response.getBody().getStatus());
    }

    @Test
    void handleValidationExceptions_ShouldReturnBadRequestWithFieldErrors() {
        // Arrange
        BindingResult bindingResult = mock(BindingResult.class);
        FieldError fieldError = new FieldError("task", "title", "must not be empty");
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError));
        
        MethodArgumentNotValidException ex = new MethodArgumentNotValidException(null, bindingResult);

        // Act
        ResponseEntity<ApiErrorResDTO> response = exceptionHandler.handleValidationExceptions(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Validation failed", response.getBody().getMessage());
        assertEquals(1, response.getBody().getErrors().size());
        assertTrue(response.getBody().getErrors().contains("must not be empty"));
    }

    @Test
    void handleAllExceptions_ShouldReturnInternalServerError() {
        // Arrange
        Exception ex = new Exception("Unexpected error occurred");

        // Act
        ResponseEntity<ApiErrorResDTO> response = exceptionHandler.handleAllExceptions(ex, request);

        // Assert
        assertNotNull(response);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Unexpected error occurred", response.getBody().getMessage());
        assertEquals("/api/tasks", response.getBody().getPath());
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), response.getBody().getStatus());
    }

    // Test enum for the MethodArgumentTypeMismatch test
    private enum TestEnum {
        VALUE1, VALUE2
    }
}
