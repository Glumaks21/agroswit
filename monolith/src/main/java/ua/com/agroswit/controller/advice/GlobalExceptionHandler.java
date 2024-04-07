package ua.com.agroswit.controller.advice;

import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ua.com.agroswit.exception.RequestValidationException;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;

import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(RequestValidationException.class)
    ProblemDetail handleBadRequest(RequestValidationException e) {
        var problemDetail = ProblemDetail.forStatus(BAD_REQUEST);
        problemDetail.setTitle("Validation error");
        return problemDetail;
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

    @ExceptionHandler(ResourceNotFoundException.class)
    ProblemDetail handleNotFound(ResourceNotFoundException e) {
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, e.getLocalizedMessage());
    }

    @ExceptionHandler(ResourceInConflictStateException.class)
    ProblemDetail handleNotFound(ResourceInConflictStateException e) {
        return ProblemDetail.forStatusAndDetail(CONFLICT, e.getLocalizedMessage());
    }
}
