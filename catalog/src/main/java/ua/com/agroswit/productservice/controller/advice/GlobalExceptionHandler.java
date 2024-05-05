package ua.com.agroswit.productservice.controller.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.agroswit.productservice.exceptions.ResourceInConflictStateException;
import ua.com.agroswit.productservice.exceptions.ResourceNotFoundException;

import java.util.*;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler  extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    @ResponseStatus(NOT_FOUND)
    ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(CONFLICT)
    ProblemDetail handleConflict(ResourceInConflictStateException ex) {
        return ProblemDetail.forStatusAndDetail(CONFLICT, ex.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(BAD_REQUEST)
    ProblemDetail handleBadRequest(IllegalArgumentException ex) {
        return ProblemDetail.forStatusAndDetail(BAD_REQUEST, ex.getLocalizedMessage());
    }

    @ExceptionHandler
    @ResponseStatus(I_AM_A_TEAPOT)
    ProblemDetail handleAllExceptions(Exception ex) {
        log.error("Unexpected error", ex);
        return ProblemDetail.forStatusAndDetail(I_AM_A_TEAPOT, ex.getLocalizedMessage());
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = new HashMap<String, Set<String>>();
        ex.getAllErrors().forEach(e -> {
            var name = e.getObjectName();
            if (e instanceof FieldError fe) {
                name = fe.getField();
            }

            errors.computeIfAbsent(name, key -> new HashSet<>()).add(e.getDefaultMessage());
        });

        var problemDetail = ex.getBody();
        problemDetail.setTitle("Validation error");
        problemDetail.setProperty("errors", errors);
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}
