package ua.com.agroswit.inventoryservice.controller.advice;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.agroswit.inventoryservice.exception.ResourceInConflictStateException;
import ua.com.agroswit.inventoryservice.exception.ResourceNotFoundException;


import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    @ResponseStatus(NOT_FOUND)
    ProblemDetail handleNotFound(ResourceNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getLocalizedMessage());
    }

    @ExceptionHandler(ResourceInConflictStateException.class)
    @ResponseStatus(CONFLICT)
    ProblemDetail handleConflict(ResourceInConflictStateException ex) {
        return ProblemDetail.forStatusAndDetail(CONFLICT, ex.getLocalizedMessage());
    }

//    @ResponseStatus(HttpStatus.I_AM_A_TEAPOT)
//    @ExceptionHandler(Exception.class)
//    public ProblemDetail handleAllExceptions(Exception ex) {
//        return ProblemDetail.forStatusAndDetail(HttpStatus.I_AM_A_TEAPOT, ex.getMessage());
//    }
}
