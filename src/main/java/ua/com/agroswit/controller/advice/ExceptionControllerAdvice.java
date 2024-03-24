package ua.com.agroswit.controller.advice;

import jakarta.validation.ValidationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ua.com.agroswit.dto.response.ErrorDescriptionDTO;
import ua.com.agroswit.exception.RequestValidationException;
import ua.com.agroswit.exception.ResourceInConflictStateException;
import ua.com.agroswit.exception.ResourceNotFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.springframework.http.HttpStatus.*;

@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(RequestValidationException.class)
    ResponseEntity<ErrorDescriptionDTO> handleBadRequest(RequestValidationException e) {
        var description = new ErrorDescriptionDTO(
                "Validation exception",
                Map.of(e.getAbout(), e.getDescription()),
                BAD_REQUEST.value());
        return new ResponseEntity<>(description, BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorDescriptionDTO> handleBadRequest(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();
        e.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });

        var description = new ErrorDescriptionDTO(
                "Validation exception",
                errors,
                BAD_REQUEST.value());
        return new ResponseEntity<>(description, BAD_REQUEST);
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    ResponseEntity<ErrorDescriptionDTO> handleNotFound(ResourceNotFoundException e) {
        var description = new ErrorDescriptionDTO(
                "Resource not found",
                Map.of("state", e.getLocalizedMessage()),
                NOT_FOUND.value());
        return new ResponseEntity<>(description, NOT_FOUND);
    }

    @ExceptionHandler(ResourceInConflictStateException.class)
    ResponseEntity<ErrorDescriptionDTO> handleNotFound(ResourceInConflictStateException e) {
        var description = new ErrorDescriptionDTO(
                "Resource state conflicts",
                Map.of("state", e.getLocalizedMessage()),
                CONFLICT.value());
        return new ResponseEntity<>(description, CONFLICT);
    }
}
