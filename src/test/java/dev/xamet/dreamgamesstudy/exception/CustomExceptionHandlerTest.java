package dev.xamet.dreamgamesstudy.exception;

import dev.xamet.dreamgamesstudy.exception.handler.CustomExceptionHandler;
import dev.xamet.dreamgamesstudy.exception.handler.ErrorResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomExceptionHandlerTest {

    private CustomExceptionHandler customExceptionHandler;

    @BeforeEach
    void setUp() {
        customExceptionHandler = new CustomExceptionHandler();
    }

    @Test
    void canHandleBadRequestException() throws Exception {
        BadRequestException badRequestException = new BadRequestException("Bad Request");

        ErrorResponse errorResponse = customExceptionHandler.handleBadRequestException(badRequestException);

        assertEquals("Bad Request", errorResponse.getMessage());
        assertEquals(400, errorResponse.getStatus());
    }

    @Test
    void canHandleNotFoundException() throws Exception {
        NotFoundException notFoundException = new NotFoundException("Not Found");

        ErrorResponse errorResponse = customExceptionHandler.handleNotFoundException(notFoundException);

        assertEquals("Not Found", errorResponse.getMessage());
        assertEquals(404, errorResponse.getStatus());

    }

    @Test
    void canHandleUnauthorizedException() {
        UnauthorizedException unauthorizedException = new UnauthorizedException("Unauthorized");

        ErrorResponse errorResponse = customExceptionHandler.handleUnauthorizedException(unauthorizedException);

        assertEquals("Unauthorized", errorResponse.getMessage());
        assertEquals(401, errorResponse.getStatus());

    }
}
