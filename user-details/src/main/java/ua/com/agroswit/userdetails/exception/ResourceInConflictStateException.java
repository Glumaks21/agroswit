package ua.com.agroswit.userdetails.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.CONFLICT;


@ResponseStatus(CONFLICT)
public class ResourceInConflictStateException extends RuntimeException {
    public ResourceInConflictStateException(String message) {
        super(message);
    }
}
