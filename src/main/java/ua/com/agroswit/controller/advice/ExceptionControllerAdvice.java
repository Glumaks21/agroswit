package ua.com.agroswit.controller.advice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.agroswit.dto.response.ErrorDescriptionDTO;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorDescriptionDTO> handleNotFound(ResourceNotFoundException e) {
        var body = new ErrorDescriptionDTO(e.getLocalizedMessage(), NOT_FOUND.value());

        return  new ResponseEntity<>(body, NOT_FOUND);
    }

    @ExceptionHandler(ResourceInConflictStateException.class)
    ResponseEntity<ErrorDescriptionDTO> handleNotFound(ResourceInConflictStateException e) {
        var body = new ErrorDescriptionDTO(e.getLocalizedMessage(), CONFLICT.value());

        return  new ResponseEntity<>(body, CONFLICT);
    }

}
