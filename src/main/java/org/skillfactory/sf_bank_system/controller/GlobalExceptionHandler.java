package org.skillfactory.sf_bank_system.controller;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.validation.ValidationException;
import org.skillfactory.sf_bank_system.model.dto.ErrorDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import java.nio.file.AccessDeniedException;
import java.util.NoSuchElementException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorDto handleRuntimeException(RuntimeException e) {
        return getErrorDto(e);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return getErrorDto(e);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        StringBuilder builder = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach((error) -> builder.append(error.getDefaultMessage()).append("\n"));
        return getErrorDto(e, builder.toString());
    }

    @ExceptionHandler(ValidationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleValidationException(ValidationException e) {
        return getErrorDto(e);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ErrorDto handleIllegalArgumentException(IllegalArgumentException e) {
        return getErrorDto(e);
    }

    @ExceptionHandler(JWTVerificationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorDto handleJWTVerificationException(JWTVerificationException e) {
        return getErrorDto(e);
    }

    @ExceptionHandler(AuthenticationException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorDto handleAuthenticationException(AuthenticationException e) {
        return getErrorDto(e, "Неверный email или пароль");
    }

    @ExceptionHandler(AccessDeniedException.class)
    @ResponseStatus(value = HttpStatus.FORBIDDEN)
    public ErrorDto handleAccessDeniedException(AccessDeniedException e) {
        return getErrorDto(e);
    }

    @ExceptionHandler(NoSuchElementException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public ErrorDto handleNoSuchElementException(NoSuchElementException e) {
        return getErrorDto(e);
    }

    private ErrorDto getErrorDto(Exception e, String errorMassage) {
        ErrorDto errorDto = extractErrors(e);
        errorDto.setMessage(errorMassage);
        return errorDto;
    }

    private ErrorDto getErrorDto(Exception e) {
        return extractErrors(e);
    }

    private ErrorDto extractErrors(Exception e) {
        StackTraceElement[] stackTrace = (e.getCause() != null) ? e.getCause().getStackTrace() : e.getStackTrace();
        if (stackTrace.length > 0) {
            return ErrorDto.builder()
                    .message(e.getMessage())
                    .classError(stackTrace[0].getFileName())
                    .methodError(stackTrace[0].getMethodName())
                    .build();
        } else {
            return ErrorDto.builder()
                    .message("UnknownError")
                    .classError("UnknownClass")
                    .methodError("unknownMethod")
                    .build();
        }
    }
}