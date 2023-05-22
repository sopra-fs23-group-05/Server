package ch.uzh.ifi.hase.soprafs23.exceptions;

import org.junit.jupiter.api.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.server.ResponseStatusException;

import javax.servlet.http.HttpServletRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;

public class GlobalExceptionAdviceTest {

    private final GlobalExceptionAdvice globalExceptionAdvice = new GlobalExceptionAdvice();

    @Test
    public void handleConflict_ShouldReturnConflictResponse() {
        IllegalArgumentException exception = new IllegalArgumentException("Test exception");
        WebRequest mockRequest = mock(WebRequest.class);

        ResponseEntity<Object> responseEntity = globalExceptionAdvice.handleConflict(exception, mockRequest);

        assertEquals(HttpStatus.CONFLICT, responseEntity.getStatusCode());
        assertEquals("This should be application specific", responseEntity.getBody());
        assertEquals(new HttpHeaders(), responseEntity.getHeaders());
    }

    @Test
    public void handleTransactionSystemException_ShouldReturnResponseStatusException() {
        Exception exception = new Exception("Test exception");
        HttpServletRequest mockRequest = new MockHttpServletRequest();

        ResponseStatusException responseStatusException = globalExceptionAdvice.handleTransactionSystemException(exception, mockRequest);

        assertEquals(HttpStatus.CONFLICT, responseStatusException.getStatus());
        assertEquals("Test exception", responseStatusException.getReason());
        assertEquals(exception, responseStatusException.getCause());
    }

    @Test
    public void handleException_ShouldReturnResponseStatusException() {

        Exception exception = new Exception("Test exception");

        ResponseStatusException responseStatusException = globalExceptionAdvice.handleException(exception);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, responseStatusException.getStatus());
        assertEquals("Test exception", responseStatusException.getReason());
        assertEquals(exception, responseStatusException.getCause());
    }
}