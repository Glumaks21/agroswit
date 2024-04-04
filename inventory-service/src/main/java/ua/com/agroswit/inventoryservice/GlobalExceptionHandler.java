package ua.com.agroswit.inventoryservice;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler
    ProblemDetail handleConflict(ResourceInConflictStateException ex) {
        return ProblemDetail.forStatusAndDetail(CONFLICT, ex.getLocalizedMessage());
    }
}
