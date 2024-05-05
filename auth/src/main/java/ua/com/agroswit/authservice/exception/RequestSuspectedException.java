package ua.com.agroswit.authservice.exception;

import org.springframework.web.bind.annotation.ResponseStatus;

import static org.springframework.http.HttpStatus.FORBIDDEN;

@ResponseStatus(FORBIDDEN)
public class RequestSuspectedException extends RuntimeException {
    public RequestSuspectedException(String message) {
        super(message);
    }
}
