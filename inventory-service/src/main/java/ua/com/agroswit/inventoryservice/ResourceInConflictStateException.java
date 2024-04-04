package ua.com.agroswit.inventoryservice;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceInConflictStateException extends RuntimeException {
    public ResourceInConflictStateException(String message) {
        super(message);
    }
}
