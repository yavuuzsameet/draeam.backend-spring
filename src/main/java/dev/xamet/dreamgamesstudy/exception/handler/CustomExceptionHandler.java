package dev.xamet.dreamgamesstudy.exception.handler;

import dev.xamet.dreamgamesstudy.exception.BadRequestException;
import dev.xamet.dreamgamesstudy.exception.NotFoundException;
import dev.xamet.dreamgamesstudy.exception.UnauthorizedException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
@RestControllerAdvice
public class CustomExceptionHandler {

    /*
     * Handle all BadRequestExceptions and return a simple error response body
     */
    @ExceptionHandler(BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBadRequestException(BadRequestException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.BAD_REQUEST.value());
    }

    /*
     * Handle all NotFoundExceptions and return a simple error response body
     */
    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleNotFoundException(NotFoundException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.NOT_FOUND.value());
    }

    /*
     * Handle all UnauthorizedExceptions and return a simple error response body
     */
    @ExceptionHandler(UnauthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ErrorResponse handleUnauthorizedException(UnauthorizedException e) {
        return new ErrorResponse(e.getMessage(), HttpStatus.UNAUTHORIZED.value());
    }

}

