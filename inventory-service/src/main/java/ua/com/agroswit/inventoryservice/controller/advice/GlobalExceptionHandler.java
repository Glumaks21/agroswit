package ua.com.agroswit.inventoryservice.controller.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.agroswit.inventoryservice.exception.ResourceInConflictStateException;
import ua.com.agroswit.inventoryservice.exception.ResourceNotFoundException;

import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

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

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        var errors = ex.getAllErrors().stream()
                .collect(Collectors.toMap(e -> {
                    if (e instanceof FieldError fe) {
                        return fe.getField();
                    }

                    return e.getObjectName();
                }, DefaultMessageSourceResolvable::getDefaultMessage));

        var problemDetail = ex.getBody();
        problemDetail.setTitle("Validation error");
        problemDetail.setProperty("errors", errors);
        return super.handleMethodArgumentNotValid(ex, headers, status, request);
    }
}
