package dev.matheuslf.desafio.inscritos.domain.exceptions;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class NotValidExceptionTest {
    
    private static final String TEST_MESSAGE = "Test validation message";

    @Test
    void shouldCreateExceptionWithMessage() {
        // when
        NotValidException exception = new NotValidException(TEST_MESSAGE);
        
        // then
        assertNotNull(exception);
        assertEquals(TEST_MESSAGE, exception.getMessage());
        assertNull(exception.getCause());
    }

}