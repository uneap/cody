package com.cody.backend.storage.api;

import com.cody.backend.storage.response.Response;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class, InvalidDataAccessApiUsageException.class})
    protected Response handleIllegalStateException(IllegalStateException e) {
        return Response.builder()
                       .reason(e.toString())
                       .statusCode(HttpStatus.SERVICE_UNAVAILABLE.value())
                       .build();
    }

    @ExceptionHandler({DataIntegrityViolationException.class,
        OptimisticLockingFailureException.class})
    protected Response handleDataIntegrityViolationException(RuntimeException e) {
        return Response.builder()
                       .reason(e.toString())
                       .statusCode(HttpStatus.LOCKED.value())
                       .build();
    }

    @ExceptionHandler({EmptyResultDataAccessException.class, EntityNotFoundException.class})
    protected Response handleEmptyResultException(RuntimeException e) {
        return Response.builder()
                       .reason(e.toString())
                       .statusCode(HttpStatus.NO_CONTENT.value())
                       .build();
    }
}
