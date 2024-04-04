package ua.com.agroswit.storageservice;

import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler
    ProblemDetail fileNotFound(FileNotFoundException ex) {
        return ProblemDetail.forStatusAndDetail(NOT_FOUND, ex.getLocalizedMessage());
    }
}
