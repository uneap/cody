package com.cody.backend.storage.api;

import com.cody.backend.storage.response.Response;
import jakarta.persistence.EntityNotFoundException;
import org.apache.kafka.common.errors.InvalidRequestException;
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
        return new Response(HttpStatus.SERVICE_UNAVAILABLE.value(), e.toString());
    }

    @ExceptionHandler({DataIntegrityViolationException.class, OptimisticLockingFailureException.class})
    protected Response handleDataIntegrityViolationException(RuntimeException e) {
        return new Response(HttpStatus.LOCKED.value(), e.toString());
    }

    @ExceptionHandler({EmptyResultDataAccessException.class, EntityNotFoundException.class})
    protected Response handleEmptyResultException(RuntimeException e) {
        return new Response(HttpStatus.NO_CONTENT.value(), e.toString());
    }

    @ExceptionHandler({InvalidRequestException.class})
    protected Response handleInvalidRequestException(RuntimeException e) {
        return new Response(HttpStatus.UNAUTHORIZED.value(), e.toString());
    }
}
